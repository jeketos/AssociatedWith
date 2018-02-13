package com.jeketos.associatedwith.ext

import android.util.Log

fun Any.logd(message: String, tag: String = javaClass.simpleName){
    Log.d(tag, message)
}

fun Any.loge(throwable: Throwable, tag: String = javaClass.simpleName) {
    Log.e(tag, "error", throwable)
}