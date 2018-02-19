package com.jeketos.associatedwith.data

import android.annotation.SuppressLint
import android.os.Parcelable
import android.view.MotionEvent
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Point(
        val action: String = "",
        val color: String = "",
        val x: Float = 0.0f,
        val y: Float = 0.0f
): Parcelable {

    enum class ACTION(val value: String){
        START("START"),
        STOP("STOP"),
        MOVE("MOVE")
    }
}

val Point.actionEnum: Point.ACTION get() = Point.ACTION.values().find { it.value ==  action }!!

fun Int.toPointAction() = when(this){
    MotionEvent.ACTION_DOWN -> "START"
    MotionEvent.ACTION_UP -> "STOP"
    MotionEvent.ACTION_MOVE -> "MOVE"
    else -> "STOP"
}