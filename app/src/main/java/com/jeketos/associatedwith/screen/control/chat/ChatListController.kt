package com.jeketos.associatedwith.screen.control.chat

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.jeketos.associatedwith.R
import com.jeketos.associatedwith.data.Message
import com.jeketos.associatedwith.ext.parkinsonClick
import com.jeketos.associatedwith.screen.lobbies.publiclobbies.Holder

class ChatListController(
        private val type: ChatListFragment.Type,
        private val onClick: (Message) -> Unit
        ): EpoxyController() {

    private val data = mutableListOf<Message>()

    override fun buildModels() {
        data.forEach {
            when(type){
                ChatListFragment.Type.GUESSER -> {
                    guesserMessage {
                        id(it.id)
                        item(it)
                    }
                }
                ChatListFragment.Type.RIDDLER -> {
                    riddlerMessage {
                        id(it.id)
                        item(it)
                        onClick { onClick(it) }
                    }
                }
            }
        }
    }

    fun updateItems(list: List<Message>) {
        data.clear()
        data.addAll(list)
        requestModelBuild()
    }
}


@EpoxyModelClass(layout = R.layout.item_chat)
abstract class  RiddlerMessageModel : MessageModel(){

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onClick: (Message) -> Unit

    override fun bind(holder: Holder) {
        super.bind(holder)
        val view = holder.view
        view.findViewById<View>(R.id.right).parkinsonClick {
            onClick(item.copy(approved = true))
        }
        view.findViewById<View>(R.id.wrong).parkinsonClick {
            onClick(item.copy(approved = false))
        }
    }

}

@EpoxyModelClass(layout = R.layout.item_chat_guesser)
abstract class  GuesserMessageModel : MessageModel()


abstract class MessageModel : EpoxyModelWithHolder<Holder>(){

    @EpoxyAttribute
    lateinit var item: Message

    override fun bind(holder: Holder) {
        val view = holder.view
        val name = view.findViewById<TextView>(R.id.name)
        val message = view.findViewById<TextView>(R.id.message)
        val context = view.context
        name.text = context.getString(R.string.name_colon, item.name)
        message.text = item.message
            when(item.approved){
                true -> {
                    name.setTextColor(ContextCompat.getColor(context, R.color.green))
                    message.setTextColor(ContextCompat.getColor(context, R.color.green))
                }
                false -> {
                    name.setTextColor(ContextCompat.getColor(context, R.color.red))
                    message.setTextColor(ContextCompat.getColor(context, R.color.red))
                }
                else -> {
                    name.setTextColor(ContextCompat.getColor(context, R.color.black))
                    message.setTextColor(ContextCompat.getColor(context, R.color.black))
                }
            }
    }

}