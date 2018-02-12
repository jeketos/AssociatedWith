package com.jeketos.associatedwith.screen.lobbies.privatelobbies

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.jeketos.associatedwith.data.PrivateLobby
import com.jeketos.associatedwith.ext.Op
import com.jeketos.associatedwith.ext.loge
import com.jeketos.associatedwith.model.LobbiesModel
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class PrivateLobbiesViewModel @Inject constructor(
        app: Application,
        private val lobbiesModel: LobbiesModel
): AndroidViewModel(app) {

    private var disposable: Disposable? = null
    var lobbiesState = MutableLiveData<LobbiesState>().apply { value = LobbiesState.Idle }

    init {
        observeLobbies()
    }

    private fun observeLobbies() {
        disposable =
                lobbiesModel.observePrivateLobbies()
                        .subscribe({
                            lobbiesState.value =  when(it.op){
                                Op.ADD -> LobbiesState.Add(it.value)
                                Op.CHANGE -> LobbiesState.Change(it.value)
                                Op.MOVE -> LobbiesState.Move(it.value)
                                Op.REMOVE -> LobbiesState.Remove(it.value)
                            }
                        },{
                            loge(it)
                        })
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}

sealed class LobbiesState{
    object Idle: LobbiesState()
    class Add(val lobby: PrivateLobby): LobbiesState()
    class Remove(val lobby: PrivateLobby): LobbiesState()
    class Change(val lobby: PrivateLobby): LobbiesState()
    class Move(val lobby: PrivateLobby): LobbiesState()
}