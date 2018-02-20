package com.jeketos.associatedwith.model

import com.jeketos.associatedwith.data.Message
import io.reactivex.Observable

interface ChatModel {
    fun observeMessages(lobbyId: String): Observable<Message>
}