package com.jeketos.associatedwith.screen.createlobby

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.jeketos.associatedwith.data.PrivateLobby
import com.jeketos.associatedwith.ext.loge
import com.jeketos.associatedwith.model.LobbiesModel
import javax.inject.Inject

class CreateLobbyViewModel @Inject constructor(
    app: Application,
    private val lobbiesModel: LobbiesModel
): AndroidViewModel(app){

    val state = MutableLiveData<CreateLobbyState>().apply { value = CreateLobbyState.Idle }

    fun createLobby(name: String, password: String) {
        lobbiesModel.createPrivateLobby(name, password)
                .subscribe({
                    state.postValue(CreateLobbyState.Created(it))
                },{
                    state.postValue(CreateLobbyState.Error(it))
                    loge(it)
                })
    }
}


sealed class CreateLobbyState{
    object Idle: CreateLobbyState()
    class Created(val lobby: PrivateLobby): CreateLobbyState()
    class Error(val error: Throwable): CreateLobbyState()
}