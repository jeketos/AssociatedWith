package com.jeketos.associatedwith.screen.control.chat

import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.ViewModel
import com.jeketos.associatedwith.data.Message
import com.jeketos.associatedwith.model.ChatModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Named

class ChatListViewModel @Inject constructor(
    @Named("lobbyId") val lobbyId: String,
    private val chatModel: ChatModel
): ViewModel(){

    val state = LiveDataReactiveStreams.fromPublisher(observeMessages())

    private fun observeMessages(): Flowable<Message> =
            chatModel.observeMessages(lobbyId).toFlowable(BackpressureStrategy.BUFFER)

}