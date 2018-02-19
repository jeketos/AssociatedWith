package com.jeketos.associatedwith.screen.control.riddle

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import com.jeketos.associatedwith.data.Lobby
import com.jeketos.associatedwith.ext.loge
import com.jeketos.associatedwith.model.LobbiesModel
import com.jeketos.associatedwith.model.RestModel
import javax.inject.Inject

class RiddlerViewModel @Inject constructor(
        private val lobby: Lobby,
        private val restModel: RestModel,
        private val lobbiesModel: LobbiesModel
): ViewModel(){

    private val state = MutableLiveData<State>().apply { value = State.Progress }

    init {
        restModel.getWords()
                .subscribe({
                    state.postValue(State.ShowWords(it))
                },{
                    loge(it)
                })
    }

    fun observe(owner: LifecycleOwner, observer: (State) -> Unit){
        state.observe(owner, Observer {
            observer.invoke(it!!)
        })
    }


    sealed class State{
        object Idle : State()
        object Progress : State()
        class ShowWords(val words: List<String>) : State()
    }

    fun onWordSelected(word: String) {
        lobbiesModel.setSelectedWord(lobby.id, word)
    }

}