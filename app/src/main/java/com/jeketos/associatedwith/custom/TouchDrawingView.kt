package com.jeketos.associatedwith.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.jeketos.associatedwith.data.Point
import com.jeketos.associatedwith.data.toPointAction
import com.jeketos.associatedwith.ext.toColorString

class TouchDrawingView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
): DrawingView(context, attrs, defStyle) {

    private var onDraw: ((Point) -> Unit)? = null

    override fun performClick(): Boolean {
        return super.performClick()
    }

    fun setOnDrawListener(onDraw:(Point) -> Unit){
        this.onDraw = onDraw
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        performClick()
        val x = event.x
        val y = event.y
        when (event.action){
            MotionEvent.ACTION_DOWN -> {
                actionDown(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                actionMove(x, y)
            }
            MotionEvent.ACTION_UP -> {
                actionUp()
            }
        }
        onDraw?.invoke(Point(
                event.action.toPointAction(),
                paint.color.toColorString(),
                x/width,
                y/width)
        )
        return true
    }

}