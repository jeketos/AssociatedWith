package com.jeketos.associatedwith.model

import com.jeketos.associatedwith.data.DataEvent
import com.jeketos.associatedwith.data.Lobby
import com.jeketos.associatedwith.data.PrivateLobby
import io.reactivex.Observable

interface LobbiesModel {
    fun observePrivateLobbies(): Observable<DataEvent<PrivateLobby>>
    fun observePublicLobbies(): Observable<DataEvent<Lobby>>
}