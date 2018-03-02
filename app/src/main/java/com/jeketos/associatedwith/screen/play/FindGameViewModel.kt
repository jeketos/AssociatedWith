package com.jeketos.associatedwith.screen.play

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.jeketos.associatedwith.data.USER_NAME
import com.jeketos.associatedwith.ext.getPreferences
import com.jeketos.associatedwith.ext.loge
import com.jeketos.associatedwith.model.RestModel
import javax.inject.Inject

class FindGameViewModel @Inject constructor(
        app: Application,
        private val restModel: RestModel
): AndroidViewModel(app) {

    val state = MutableLiveData<State>().apply { value = State.Idle }

    init {
       if(getApplication<Application>().getPreferences().getString(USER_NAME, "").isEmpty()){
           state.value = State.EnterName
       }
    }

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
        object EnterName: State()
        class  OnGameFind(val lobbyId: String): State()
        class  Error(val error: Throwable): State()
    }

}