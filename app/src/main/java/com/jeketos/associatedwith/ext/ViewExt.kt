package com.jeketos.associatedwith.ext

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
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
    dividerItemDecoration.setDrawable(drawable!!)
    return dividerItemDecoration
}

fun RecyclerView.addDividerItemDecoration(){
    addItemDecoration(getDividerItemDecoration(context))
}

fun EditText.setEnterKeyListener(onPressed: (View) -> Unit){
    setOnKeyListener { v, keyCode, event ->
        if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN){
            onPressed.invoke(this)
        }
            false
    }
}

fun RecyclerView.setScrollToLast(){
    adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            val linearLayoutManager = layoutManager as LinearLayoutManager
            if(positionStart == 0){
                linearLayoutManager.scrollToPosition(adapter.itemCount)
            } else {
                val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()
                if (lastVisibleItemPosition >= positionStart + itemCount - 2) {
                    linearLayoutManager.scrollToPosition(positionStart+itemCount - 1)
                }
            }
        }
    })
}
