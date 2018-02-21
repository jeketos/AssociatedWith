package com.jeketos.associatedwith.data

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Message(
        @get:Exclude
        val id: String = "",
        val userId: String = "",
        val name: String = "",
        val message: String = "",
        val approved: Boolean? = null
): Parcelable

fun DataSnapshot.toMessage(): Message = getValue(Message::class.java)!!.copy(id = this.key)
