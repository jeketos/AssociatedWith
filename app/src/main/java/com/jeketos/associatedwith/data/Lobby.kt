package com.jeketos.associatedwith.data

import com.google.firebase.database.Exclude

data class Lobby(
        val name: String = "",
        @get:Exclude val members: List<String> = emptyList()
)

data class PrivateLobby(
        val name: String = "",
        val password: String = "",
        @get:Exclude val members: List<String> = emptyList()
)