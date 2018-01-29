package com.jeketos.associatedwith.ext

import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

fun View.parkinsonClick(onClick: (View) -> Unit){
    RxView.clicks(this)
            .takeUntil(RxView.detaches(this))
            .throttleFirst(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({},{})
}