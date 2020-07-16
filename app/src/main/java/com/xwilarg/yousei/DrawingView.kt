package com.xwilarg.yousei

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView : View {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        isFocusable = true;
        isFocusableInTouchMode = true;
        paint = Paint()
        paint.strokeWidth = 10f
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        paint.isDither = true
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        path = Path()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event!!.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> path.lineTo(x, y)
            else -> return false
        }
        postInvalidate()
        return true
    }

    val paint: Paint
    var path: Path
}