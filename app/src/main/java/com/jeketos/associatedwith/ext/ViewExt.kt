package com.jeketos.associatedwith.ext

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.jeketos.associatedwith.R
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

fun View.parkinsonClick(onClick: (View) -> Unit){
    RxView.clicks(this)
            .takeUntil(RxView.detaches(this))
            .throttleFirst(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onClick.invoke(this)
            },{})
}

fun getDividerItemDecoration(context: Context): DividerItemDecoration {
    val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    val drawable = ContextCompat.getDrawable(context, R.drawable.line_divider)
    dividerItemDecoration.setDrawable(drawable)
    return dividerItemDecoration
}

fun RecyclerView.addDividerItemDecoration(){
    addItemDecoration(getDividerItemDecoration(context))
}