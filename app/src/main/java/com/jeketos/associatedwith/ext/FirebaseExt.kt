package com.jeketos.associatedwith.ext

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.database.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1

class ValueListener (
        private val onDataChangeBlock: (DataSnapshot) -> Unit,
        private val onCancelledBlock: (DatabaseError) -> Unit = {}
) : ValueEventListener {

    override fun onDataChange(data: DataSnapshot) {
        onDataChangeBlock(data)
    }

    override fun onCancelled(error: DatabaseError) {
        onCancelledBlock(error)
    }
}

fun <TResult> Task<TResult>.toRxCompletable(): Completable {
    return Completable.create { emitter ->
        addOnCompleteListener {
            if (it.isSuccessful) {
                emitter.onComplete()
            } else {
                emitter.onError(it.exception!!)
            }
        }
    }
}

fun <TResult> Task<TResult>.toRxSingle(): Single<TResult> {
    return Single.create { emitter ->
        addOnCompleteListener {
            if (it.isSuccessful) {
                emitter.onSuccess(it.result)
            } else {
                emitter.onError(it.exception!!)
            }
        }
    }
}

fun <R> toRxCompletable(task: () -> Task<R>): Completable {
    return Completable.create { emitter ->
        task().addOnCompleteListener {
            if (it.isSuccessful) {
                emitter.onComplete()
            } else {
                emitter.onError(it.exception!!)
            }
        }
    }
}

fun <R> toRxSingle(task: () -> Task<R>): Single<R> {
    return Single.create { emitter ->
        task().addOnCompleteListener {
            if (it.isSuccessful) {
                emitter.onSuccess(it.result)
            } else {
                emitter.onError(it.exception!!)
            }
        }
    }
}

fun <R> toRxMaybe(task: () -> Task<R>): Maybe<R> {
    return Maybe.create { emitter ->
        task().addOnCompleteListener {
            if (it.isSuccessful) {
                if(it.result == null) {
                    emitter.onComplete()
                } else {
                    emitter.onSuccess(it.result)
                }
            } else {
                emitter.onError(it.exception!!)
            }
        }
    }
}

fun <T> Completable.andThenSingleTask(task: () -> Task<T>): Single<T> {
    return andThen(toRxSingle { task() })
}

fun <T> Completable.andThenMaybeTask(task: () -> Task<T>): Maybe<T> {
    return andThen(toRxMaybe { task() })
}

fun <T> Completable.andThenCompletableTask(task: () -> Task<T>): Completable {
    return andThen(toRxCompletable { task() })
}

fun DatabaseReference.setValueRx(value: Any?): Completable {
    return toRxCompletable { setValue(value) }
}

fun DatabaseReference.updateChildrenRx(vararg pairs: Pair<String, Any?>): Completable {
    return updateChildrenRx(mapOf(*pairs))
}

fun DatabaseReference.updateChildrenRx(map: Map<String, Any?>): Completable {
    return toRxCompletable { updateChildren(map) }
}

inline fun <reified T> DatabaseReference.pushRxValue(
        value: T,
        idProperty: KMutableProperty1<T, String>?
): Single<T> {
    val push = ref.push()
    idProperty?.set(value, push.key)
    return push.setValue(value).toRxCompletable().toSingleDefault(value)
}

fun Query.getChildIdList(): Single<List<String>> {
    return getRxSingleSnapshot().map {
        it.children.map { it.key }
    }
}

inline fun <reified T: Any> DataSnapshot.getValueList(
        idProperty: KMutableProperty1<T, String>?
): List<T> {
    return if(value == null) {
        emptyList()
    } else {
        children.map {
            it.getValue(T::class.java)!!.apply {
                idProperty?.set(this, it.key)
            }
        }
    }
}

inline fun <reified T: Any> DataSnapshot.getValue(
        default: T,
        idProperty: KMutableProperty1<T, String>?
) : T {
    return if (value == null) {
        default
    } else {
        getValue(T::class.java)!!.apply {
            idProperty?.set(this, key)
        }
    }
}

inline fun <reified T: Any> DataSnapshot.getValue(
        idProperty: KMutableProperty1<T, String>
) : T? {
    return value?.let {
        getValue(T::class.java)!!.apply {
            idProperty.set(this, key)
        }
    }
}

fun Query.getRxSingleSnapshot(): Single<DataSnapshot> {
    return Single.create<DataSnapshot> { emitter ->
        addListenerForSingleValueEvent(ValueListener(
                { snapshot -> emitter.onSuccess(snapshot) },
                { error -> emitter.onError(error.toException()) }
        ))
    }
}

fun DatabaseReference.runRxTransactionAddValue(value: Int): Single<DataSnapshot> {
    return runRxTransactionAddValue(value.toLong())
}

fun DatabaseReference.runRxTransactionAddValue(value: Long): Single<DataSnapshot> {
    return runRxTransaction { data ->
        data.apply { this.value = getValue(0) + value }
    }
}

fun DatabaseReference.runRxTransaction(func:(MutableData)-> MutableData): Single<DataSnapshot> {
    return Single.create<DataSnapshot> {
        emitter ->
        runTransaction(object: Transaction.Handler {
            override fun onComplete(error: DatabaseError?, finished: Boolean, finalData: DataSnapshot?) {
                if(error != null){
                    emitter.onError(error.toException())
                }
                if(finished){
                    emitter.onSuccess(finalData!!)
                } else {
                    emitter.onError(FirebaseException("Transaction aborted"))
                }
            }
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                return Transaction.success(func(mutableData))
            }
        })
    }
}

fun Query.getRxObservableSnapshot(): Observable<DataSnapshot> {
    return Observable.create<DataSnapshot> { emitter ->
        val listener = ValueListener(
                { snapshot ->
                    Log.d("TAG", "snapshot: ${snapshot.value}")
                    emitter.onNext(snapshot)
                },
                { error -> emitter.onError(error.toException()) }
        )
        emitter.setCancellable { removeEventListener(listener) }

        addValueEventListener(listener)
    }
}

enum class Op {
    ADD, MOVE, CHANGE, REMOVE
}

data class FirebaseEvent (
        val op: Op,
        val snapshot: DataSnapshot?,
        val previousChildName: String? = null
)

fun Query.getRxObservableChildSnapshot(): Observable<FirebaseEvent> {
    return Observable.create<FirebaseEvent> { emitter ->
        val childListener = object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                emitter.onNext(FirebaseEvent(Op.ADD, snapshot, previousChildName))
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                emitter.onNext(FirebaseEvent(Op.MOVE, snapshot, previousChildName))
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                emitter.onNext(FirebaseEvent(Op.CHANGE, snapshot, previousChildName))
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                emitter.onNext(FirebaseEvent(Op.REMOVE, snapshot))
            }

            override fun onCancelled(error: DatabaseError) {
                emitter.onError(error.toException())
            }
        }
        emitter.setCancellable { removeEventListener(childListener) }

        addChildEventListener(childListener)
    }
}

fun FirebaseDatabase.checkConnection(): Completable {
    return getRxObservableConnection().firstOrError()
            .map { connected -> if(!connected) throw Exception() }
            .toCompletable()
}

private fun FirebaseDatabase.getRxObservableConnection(): Observable<Boolean> =
        getReference(".info/connected").getRxObservableSnapshot()
                .map { it.getValue(Boolean::class.java) }

fun getFirebaseServerTime(): Single<Long> = FirebaseDatabase.getInstance()
        .getReference(".info/serverTimeOffset").getRxSingleSnapshot()
        .map { it.getValue(Long::class.java) }
        .map { offset -> System.currentTimeMillis() + offset!! }

fun checkId(id: String?, name: String) {
    if(id == null || id.isBlank()) {
        throw IllegalArgumentException("${name.capitalize()} id is empty")
    }
}

fun <T: Any>DataSnapshot.getValue(clazz: KClass<T>) = getValue(clazz.java)

inline fun <reified T: Number> MutableData.getValue(default: T): T =
        if(value == null) default else getValue(T::class.java)!!

fun <V> Map<String, V>.addToPath(keyPrefix: String = ""): Array<Pair<String, V>> =
        mapKeys { "$keyPrefix/${it.key}" }.map { it.key to it.value }.toTypedArray()
