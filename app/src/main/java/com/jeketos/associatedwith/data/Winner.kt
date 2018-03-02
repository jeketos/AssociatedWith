package com.jeketos.associatedwith.data

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Winner(val userId: String = "", val nextLobbyId: String = "") : Parcelable