package com.jeketos.associatedwith.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

open class DrawingView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
): View(context, attrs, defStyle) {

    private var  bitmap : Bitmap? = null
    private lateinit var  canvas : Canvas
    private var path : Path = Path()
    private var  bitmapPaint : Paint = Paint(Paint.DITHER_FLAG)
    private var  paint : Paint = Paint()
    private var bitmapWidth : Int = 0
    private var bitmapHeight : Int = 0
    private var mX = 0f
    private var mY = 0f
    private val touchTolerance = 4f
    private var density = 0


    init {
        with(paint){
            isAntiAlias = true
            isDither = true
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 12f
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmapWidth = w
        bitmapHeight = h
        density = context.resources.displayMetrics.densityDpi
        initDrawBitmap()
    }

    private fun initDrawBitmap() {
        if(bitmap == null) {
            val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            canvas = Canvas(newBitmap)
            canvas.drawColor(Color.TRANSPARENT)
            bitmap = newBitmap
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap,0f,0f,bitmapPaint)
        canvas.drawPath(path,paint)
    }

    fun actionDown(x: Float, y: Float){
        path.reset()
        path.moveTo(x, y)
        canvas.drawPoint(x, y, paint)
        mX = x
        mY = y
    }

    fun actionMove(x: Float,y: Float){
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)
        if(dx >= touchTolerance || dy >= touchTolerance){
            path.quadTo(mX, mY, (x+mX)/2, (y+mY)/2)
            mX = x
            mY = y
        }
    }

    fun actionUp(){
        path.lineTo(mX, mY)
        canvas.drawPath(path, paint)
        path.reset()
    }

    fun clear(){
        if(width > 0 && height > 0) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE)
            invalidate()
        }
    }

    fun setDrawColor(selectedColor: Int) {
        paint.color = selectedColor
    }

    fun setStrokeWidth(strokeWidth: Float) {
        paint.strokeWidth = strokeWidth
    }
}