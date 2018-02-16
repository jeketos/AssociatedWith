package com.jeketos.associatedwith.screen.play

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.jeketos.associatedwith.ext.loge
import com.jeketos.associatedwith.model.RestModel
import javax.inject.Inject

class FindGameViewModel @Inject constructor(
        app: Application,
        private val restModel: RestModel
): AndroidViewModel(app) {

    val state = MutableLiveData<State>().apply { value = State.Idle }

    fun findGame(){
        state.postValue(State.Progress)
        restModel.findGame()
                .subscribe({
                    state.postValue(State.OnGameFind(it))
                },{
                    state.postValue(State.Error(it))
                    loge(it)
                })

    }

    sealed class State{
        object Idle: State()
        object Progress: State()
        class  OnGameFind(val lobbyId: String): State()
        class  Error(val error: Throwable): State()
    }

}