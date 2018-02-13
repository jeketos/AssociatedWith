package com.jeketos.associatedwith.screen.lobbies.privatelobbies

import android.view.View
import com.airbnb.epoxy.EpoxyAdapter
import com.airbnb.epoxy.EpoxyModel
import com.jeketos.associatedwith.R
import com.jeketos.associatedwith.data.PrivateLobby
import kotlinx.android.synthetic.main.item_private_lobby.view.*

class PrivateLobbiesAdapter: EpoxyAdapter() {

    fun updateItem(lobby: PrivateLobby){
        val model = models.find { (it as PrivateLobbyModel).item.id == lobby.id } as PrivateLobbyModel?
        if(model != null){
            model.item = lobby
            notifyModelChanged(model)
        } else {
            addModel(PrivateLobbyModel(lobby))
        }
    }

    fun removeItem(lobby: PrivateLobby){
        removeModel(models.find { (it as PrivateLobbyModel).item.id == lobby.id })
    }

    inner class PrivateLobbyModel(var item: PrivateLobby): EpoxyModel<View>(){

        override fun getDefaultLayout(): Int = R.layout.item_private_lobby

        override fun bind(view: View) {
            with(view){
                lobbyName.text = item.name
                membersCount.text = item.members.size.toString()
            }
        }
    }

}