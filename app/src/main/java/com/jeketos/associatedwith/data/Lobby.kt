package com.jeketos.associatedwith.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Exclude

data class Lobby(
        val id: String = "",
        val name: String = "",
        @get:Exclude val members: List<Member> = emptyList()
)

data class PrivateLobby(
        val id: String = "",
        val name: String = "",
        val password: String = "",
        @get:Exclude val members: List<Member> = emptyList()
)

data class Member(val id: String = "", val drawer: Boolean = false)

fun DataSnapshot.toMember(): Member = Member(key, value as Boolean)
