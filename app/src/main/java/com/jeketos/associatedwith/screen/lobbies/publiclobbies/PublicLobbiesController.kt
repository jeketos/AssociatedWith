package com.jeketos.associatedwith.screen.lobbies.publiclobbies

import android.view.View
import com.airbnb.epoxy.*
import com.jeketos.associatedwith.R
import com.jeketos.associatedwith.data.Lobby
import kotlinx.android.synthetic.main.item_public_lobby.view.*

class PublicLobbiesController: EpoxyController() {

    val data = mutableListOf<Lobby>()

    fun updateItem(item: Lobby){
        val find = data.find { it.id == item.id }
        if(find != null){
            data[data.indexOf(find)] = item
        } else {
            data.add(item)
        }
        requestModelBuild()
    }

    fun removeItem(item: Lobby){
        data.remove(data.find { it.id == item.id })
        requestModelBuild()
    }

    override fun buildModels() {
        data.forEach {
            publicLobby {
                item(it)
                id(it.id)
            }
        }
    }

}

@EpoxyModelClass(layout = R.layout.item_public_lobby)
abstract class  PublicLobbyModel : EpoxyModelWithHolder<Holder>(){

    @EpoxyAttribute lateinit var item: Lobby

    override fun bind(holder: Holder) {
        val view = holder.view
        with(view){
            lobbyName.text = item.name
            membersCount.text = item.members.size.toString()
        }
    }

}

class Holder : EpoxyHolder(){

    lateinit var view: View

    override fun bindView(itemView: View) {
        view = itemView
    }
}