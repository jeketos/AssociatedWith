package com.jeketos.associatedwith.screen.control.guess

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import com.jeketos.associatedwith.data.Lobby
import com.jeketos.associatedwith.data.Point
import com.jeketos.associatedwith.ext.loge
import com.jeketos.associatedwith.model.DrawModel
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class GuesserViewModel @Inject constructor(
    private val lobby: Lobby,
    private val drawModel: DrawModel
): ViewModel(){

    private val state = MutableLiveData<State>().apply { value = State.Idle }
    private var disposable: Disposable? = null
//    init {
//        observePoints()
//    }

    fun observePoints() {
        disposable?.dispose()
        disposable = drawModel.observePoints(lobby.id)
                .subscribe({
                    state.value = State.OnPoint(it)
                },{loge(it)})
    }

    fun observeState(owner: LifecycleOwner, stateObserver: (State) -> Unit){
        state.observe(owner, Observer { stateObserver.invoke(it!!) })
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }

    sealed class State{
        object Idle: State()
        class OnPoint(val point: Point): State()
    }

}