package com.jeketos.associatedwith.ext

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

fun FragmentManager.replaceNow(fragment: Fragment, @IdRes containerId: Int){
    beginTransaction().replace(containerId, fragment).commitNow()
}

fun FragmentManager.replace(fragment: Fragment, @IdRes containerId: Int){
    beginTransaction().replace(containerId, fragment).commit()
}