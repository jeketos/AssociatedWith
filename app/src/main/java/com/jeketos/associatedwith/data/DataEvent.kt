package com.jeketos.associatedwith.data

import com.jeketos.associatedwith.ext.Op

data class DataEvent<out T> (
        val op: Op,
        val value: T
)
