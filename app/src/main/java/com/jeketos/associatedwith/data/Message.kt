package com.jeketos.associatedwith.data

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.firebase.database.DataSnapshot
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Message(
        val id: String = "",
        val userId: String = "",
        val name: String = "",
        val message: String = "",
        val approved: Boolean? = null
): Parcelable

fun DataSnapshot.toMessage(): Message = getValue(Message::class.java)!!.copy(id = this.key)
