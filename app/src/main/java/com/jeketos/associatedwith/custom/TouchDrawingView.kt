package com.jeketos.associatedwith.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

class TouchDrawingView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
): DrawingView(context, attrs, defStyle) {

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        performClick()
        val x = event.x
        val y = event.y

        when (event.action){
            MotionEvent.ACTION_DOWN -> {
                actionDown(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                actionMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                actionUp()
                invalidate()
            }
        }
        return true
    }

}