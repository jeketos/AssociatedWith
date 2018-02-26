package com.jeketos.associatedwith.screen.control

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.jeketos.associatedwith.data.Lobby
import com.jeketos.associatedwith.data.USER_ID
import com.jeketos.associatedwith.data.Winner
import com.jeketos.associatedwith.ext.get
import com.jeketos.associatedwith.ext.getPreferences
import com.jeketos.associatedwith.ext.loge
import com.jeketos.associatedwith.model.LobbiesModel
import javax.inject.Inject
import javax.inject.Named

class GameControlViewModel @Inject constructor(
        @Named("lobbyId") private val lobbyId: String,
        private val lobbiesModel: LobbiesModel,
        private val context: Context
): ViewModel() {

    val state = MutableLiveData<State>().apply { value = State.Progress }

    init {
        lobbiesModel.getLobby(lobbyId)
                .subscribe({
                    val member = it.members.find{ it.id == context.getPreferences().get<String>(USER_ID)}!!
                    state.postValue(if(member.drawer) State.Riddler(it) else State.Guesser(it))
                },{
                    loge(it)
                })
        observeWinner()
    }

    private fun observeWinner() {
        lobbiesModel.observeWinner(lobbyId)
                .subscribe({
                    state.value = State.OnWinner(it)
                },{loge(it)})
    }


    sealed class State{
        object Progress: State()
        class  Riddler(val lobby: Lobby): State()
        class  Guesser(val lobby: Lobby): State()
        class  OnWinner(val winner: Winner): State()
    }

}