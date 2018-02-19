package com.jeketos.associatedwith.model

import com.jeketos.associatedwith.data.DataEvent
import com.jeketos.associatedwith.data.Lobby
import com.jeketos.associatedwith.data.PrivateLobby
import io.reactivex.Observable
import io.reactivex.Single

interface LobbiesModel {
    fun observePrivateLobbies(): Observable<DataEvent<PrivateLobby>>
    fun observePublicLobbies(): Observable<DataEvent<Lobby>>
    fun createPrivateLobby(name: String, password: String): Single<PrivateLobby>
    fun getLobby(lobbyId: String): Single<Lobby>
    fun setSelectedWord(lobbyId: String, word: String)
}