package com.jeketos.associatedwith.ext

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

fun <T> Observable<T>.subscribeIoObserveMain(): Observable<T> {
    return subscribeOnIo()
            .observeOnMainThread()
}

fun <T> Observable<T>.subscribeOnIo(): Observable<T> = subscribeOn(Schedulers.io())

fun <T> Observable<T>.observeOnMainThread(): Observable<T> =
        observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.subscribeIoObserveMain(): Single<T> {
    return subscribeOnIo()
            .observeOnMainThread()
}

fun <T> Single<T>.subscribeOnIo(): Single<T> = subscribeOn(Schedulers.io())

fun <T> Single<T>.observeOnMainThread(): Single<T> = observeOn(AndroidSchedulers.mainThread())

fun Completable.subscribeIoObserveMain(): Completable {
    return subscribeOnIo()
            .observeOnMainThread()
}

fun Completable.subscribeOnIo(): Completable = subscribeOn(Schedulers.io())

fun Completable.observeOnMainThread(): Completable = observeOn(AndroidSchedulers.mainThread())

//fun RecyclerView.addScrollObservable(): Observable<Int> =
//        createScrollObservable().distinctUntilChanged()
//                .observeOnMainThread()

fun createDelay(value: Long, timeUnit: TimeUnit) =
            Observable.just(Unit)
                .delay(1, TimeUnit.MINUTES)

fun Disposable.addTo(disposable: CompositeDisposable){
    disposable.add(this)
}

//private fun RecyclerView.createScrollObservable(): Observable<Int> {
//
//    return Observable.create({ emitter ->
//        var lastOnNextValue = 0
//        val listener = object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if(!emitter.isDisposed && adapter.itemCount > 0){
//                    val layoutManager = layoutManager as LinearLayoutManager
//                    val visiblePosition = layoutManager.findLastVisibleItemPosition()
//                    val count = adapter.itemCount - 2
//                    if(visiblePosition >= count && lastOnNextValue < count && isOnline(context) ){
//                        lastOnNextValue = adapter.itemCount
//                        emitter.onNext(adapter.itemCount)
//                    }
//
//                }
//            }
//        }
//        addOnScrollListener(listener)
//        if (adapter.itemCount == 0){
//            emitter.onNext(adapter.itemCount)
//        }
//        emitter.setCancellable { removeOnScrollListener(listener) }
//    })
//}

//fun RecyclerView.observeFirstVisiblePosition(): Observable<Int> =
//        createFirstVisiblePositionObservable().distinctUntilChanged()
//                .observeOnMainThread()
//
//private fun RecyclerView.createFirstVisiblePositionObservable(): Observable<Int> {
//
//    return Observable.create({ emitter ->
//        val listener = object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val layoutManager = layoutManager as LinearLayoutManager
//                val visiblePosition = layoutManager.findFirstVisibleItemPosition()
//                if(!emitter.isDisposed && adapter.itemCount > 0 && isOnline(context)){
//                    emitter.onNext(visiblePosition)
//                }
//            }
//        }
//        addOnScrollListener(listener)
//        emitter.setCancellable { removeOnScrollListener(listener) }
//    })
//}


fun <T> Observable<T>.takeUntil(targetFragment: Fragment, disposeEvent: LifecycleEvent): Observable<T> {
    val disposeSubject = BehaviorSubject.create<Any>()

    targetFragment.fragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks(){

        override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
            super.onFragmentPaused(fm, f)
            checkFragmentLifecycleCallbacks(fm, LifecycleEvent.PAUSED, targetFragment, f)
        }
        override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
            super.onFragmentStopped(fm, f)
            checkFragmentLifecycleCallbacks(fm, LifecycleEvent.STOPPED, targetFragment, f)
        }
        override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
            super.onFragmentViewDestroyed(fm, f)
            checkFragmentLifecycleCallbacks(fm, LifecycleEvent.VIEW_DESTROYED, targetFragment, f)
        }
        override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
            super.onFragmentDestroyed(fm, f)
            checkFragmentLifecycleCallbacks(fm, LifecycleEvent.DESTROYED, targetFragment, f)
        }

        fun checkFragmentLifecycleCallbacks(
                fm: FragmentManager, callbackEvent: LifecycleEvent,
                targetFragment: Fragment, callbackFragment: Fragment) {
            if(targetFragment === callbackFragment && callbackEvent === disposeEvent) {
                disposeSubject.onNext(Unit)
                fm.unregisterFragmentLifecycleCallbacks(this)
            }
        }
    }, false)

    return takeUntil(disposeSubject)
}

fun <T> Observable<T>.disposeWhen(activity: AppCompatActivity, disposeEvent: Lifecycle.Event): Observable<T> {
    val composite = CompositeDisposable()
    val observer = object : DisposeObserver{
        override fun onPause() {
            checkDispose(Lifecycle.Event.ON_PAUSE)
        }

        override fun onStop() {
            checkDispose(Lifecycle.Event.ON_STOP)
        }

        override fun onDestroy() {
            checkDispose(Lifecycle.Event.ON_DESTROY)
        }

        override fun checkDispose(currentEvent: Lifecycle.Event) {
            if(currentEvent == disposeEvent){
                activity.lifecycle.removeObserver(this)
                composite.dispose()
            }
        }

    }
    activity.lifecycle.addObserver(observer)
    return doOnSubscribe { composite.add(it) }

}

interface DisposeObserver : LifecycleObserver{

     @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
     fun onPause()

     @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
     fun onStop()

     @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
     fun onDestroy()

    fun checkDispose(currentEvent: Lifecycle.Event)
}

fun <T> Observable<T>.disposeWhen(targetFragment: Fragment, disposeEvent: LifecycleEvent): Observable<T> {
    val composite = CompositeDisposable()

    targetFragment.fragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks(){

        override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
            super.onFragmentPaused(fm, f)
            checkFragmentLifecycleCallbacks(fm, LifecycleEvent.PAUSED, f)
        }
        override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
            super.onFragmentStopped(fm, f)
            checkFragmentLifecycleCallbacks(fm, LifecycleEvent.STOPPED, f)
        }
        override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
            super.onFragmentViewDestroyed(fm, f)
            checkFragmentLifecycleCallbacks(fm, LifecycleEvent.VIEW_DESTROYED, f)
        }
        override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
            super.onFragmentDestroyed(fm, f)
            checkFragmentLifecycleCallbacks(fm, LifecycleEvent.DESTROYED, f)
        }

        fun checkFragmentLifecycleCallbacks(
                fm: FragmentManager, callbackEvent: LifecycleEvent, callbackFragment: Fragment) {
            if(targetFragment === callbackFragment && callbackEvent === disposeEvent) {
                composite.dispose()
                fm.unregisterFragmentLifecycleCallbacks(this)
            }
        }
    }, false)

    return doOnSubscribe { composite.add(it) }
}

fun <T> Single<T>.disposeWhen(targetFragment: Fragment, disposeEvent: LifecycleEvent): Single<T> {
    val composite = CompositeDisposable()

    targetFragment.fragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks(){

        override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
            super.onFragmentPaused(fm, f)
            checkFragmentLifecycleCallbacks(fm, LifecycleEvent.PAUSED, f)
        }
        override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
            super.onFragmentStopped(fm, f)
            checkFragmentLifecycleCallbacks(fm, LifecycleEvent.STOPPED, f)
        }
        override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
            super.onFragmentViewDestroyed(fm, f)
            checkFragmentLifecycleCallbacks(fm, LifecycleEvent.VIEW_DESTROYED, f)
        }
        override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
            super.onFragmentDestroyed(fm, f)
            checkFragmentLifecycleCallbacks(fm, LifecycleEvent.DESTROYED, f)
        }

        fun checkFragmentLifecycleCallbacks(
                fm: FragmentManager, callbackEvent: LifecycleEvent, callbackFragment: Fragment) {
            if(targetFragment === callbackFragment && callbackEvent === disposeEvent) {
                composite.dispose()
                fm.unregisterFragmentLifecycleCallbacks(this)
            }
        }
    }, false)

    return doOnSubscribe { composite.add(it) }
}

enum class LifecycleEvent {
    PAUSED, STOPPED, VIEW_DESTROYED, DESTROYED
}