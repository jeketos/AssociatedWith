package com.jeketos.associatedwith.data

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Lobby(
        val id: String = "",
        val name: String = "",
        @get:Exclude val members: List<Member> = emptyList()
): Parcelable

data class PrivateLobby(
        val id: String = "",
        val name: String = "",
        val password: String = "",
        @get:Exclude val members: List<Member> = emptyList()
)

@SuppressLint("ParcelCreator")
@Parcelize
data class Member(val id: String = "", val drawer: Boolean = false): Parcelable

fun DataSnapshot.toMember(): Member = Member(key, value as Boolean)
