package com.jeketos.associatedwith.screen.control.chat

import android.support.v4.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.jeketos.associatedwith.R
import com.jeketos.associatedwith.data.Message
import com.jeketos.associatedwith.screen.lobbies.publiclobbies.Holder
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatListController: EpoxyController() {

    private val data = mutableListOf<Message>()

    fun updateItem(item: Message){
        val find = data.find { it.id == item.id }
        if(find != null){
            data[data.indexOf(find)] = item
        } else {
            data.add(item)
        }
        requestModelBuild()
    }

    override fun buildModels() {
        data.forEach {
            message {
                id(it.id)
                item(it)
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
abstract class  MessageModel : EpoxyModelWithHolder<Holder>(){

    @EpoxyAttribute
    lateinit var item: Message

    override fun bind(holder: Holder) {
        val view = holder.view
        with(view){
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

}