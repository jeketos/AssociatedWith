package com.jeketos.associatedwith.ext

import android.graphics.Color

fun Int.toColorString() : String =
    StringBuilder().append("#",
            Color.alpha(this).toString(16),
            Color.red(this).toString(16),
            Color.green(this).toString(16),
            Color.blue(this).toString(16)
    ).toString().toUpperCase()

fun String.toColorInt(): Int = Color.parseColor(this)