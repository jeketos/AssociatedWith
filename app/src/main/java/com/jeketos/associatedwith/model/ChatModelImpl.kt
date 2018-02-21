package com.jeketos.associatedwith.model

import com.google.firebase.database.DatabaseReference
import com.jeketos.associatedwith.data.Message
import com.jeketos.associatedwith.data.Nodes
import com.jeketos.associatedwith.data.toMessage
import com.jeketos.associatedwith.ext.Op
import com.jeketos.associatedwith.ext.getRxObservableChildSnapshot
import com.jeketos.associatedwith.ext.loge
import com.jeketos.associatedwith.ext.setValueRx
import io.reactivex.Observable
import javax.inject.Inject

class ChatModelImpl @Inject constructor(
        rootNode: DatabaseReference
): ChatModel{

    private val chatNode = rootNode.child(Nodes.chats)!!

    override fun observeMessages(lobbyId: String): Observable<List<Message>> {
        return chatNode.child(lobbyId).getRxObservableChildSnapshot()
                .filter { it.op == Op.ADD || it.op == Op.CHANGE }
                .map { it.snapshot!!.toMessage() }
                .scan(mutableListOf<Message>(), { list, item ->
                    list.apply {
                        val find = find { it.id == item.id }
                        if(find != null){
                            this[this.indexOf(find)] = item
                        } else {
                            add(item)
                        }
                    }
                })
                .map { it.toList() }
    }

    override fun sendMessage(lobbyId: String, message: Message) {
        chatNode.child(lobbyId).push()
                .setValueRx(message)
                .subscribe({},{loge(it)})
    }
}