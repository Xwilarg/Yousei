package com.xwilarg.yousei.quizz

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.xwilarg.yousei.R

class QuizzDrawActivity : QuizzCommon() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz_draw)
        var drawView = findViewById<View>(R.id.viewDraw)
        paint = Paint()
        paint.strokeWidth = 3f
        paint.color = 0x000
        paint.isAntiAlias = true
        paint.isDither = true
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        path = Path()
        btm = Bitmap.createBitmap(400, 200, Bitmap.Config.ARGB_8888)
        canvas = Canvas(btm)
        drawView.setOnTouchListener { v, event ->
            var x = event.x
            var y = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    path.reset()
                    path.moveTo(x, y)
                    px = x
                    py = y
                    v.invalidate();
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = Math.abs(x - px)
                    val dy = Math.abs(y - py)
                    if (dx >= 4 || dy >= 4) {
                        path.quadTo(px, py, (x + px) / 2, (y + py) / 2)
                        px = x
                        py = y
                    }
                    v.invalidate();
                }
                MotionEvent.ACTION_UP -> {
                    path.lineTo(px, py)
                    canvas.drawPath(path, paint)
                    path.reset()
                    v.performClick()
                    v.invalidate();
                }
            }
            v.onTouchEvent(event)
        }
        preload()
    }

    fun answer(view: View) {
    }

    lateinit var paint: Paint
    lateinit var path: Path
    lateinit var btm: Bitmap
    lateinit var canvas: Canvas
    var px: Float = 0f
    var py: Float = 0f
}