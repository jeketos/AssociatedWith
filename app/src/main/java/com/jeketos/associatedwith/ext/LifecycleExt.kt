package com.jeketos.associatedwith.ext

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

fun <T: ViewModel> ViewModelProvider.get(clazz: KClass<T>) = get(clazz.java)


class KObserver<T>(private val onChanged: (T) -> Unit): Observer<T> {

    override fun onChanged(t: T?) {
        onChanged.invoke(t!!)
    }

}