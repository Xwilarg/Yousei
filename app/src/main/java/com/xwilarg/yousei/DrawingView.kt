package com.xwilarg.yousei

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.google.mlkit.vision.digitalink.*
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager

class DrawingView : View {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        isFocusable = true;
        isFocusableInTouchMode = true;
        paint = Paint()
        paint.strokeWidth = 20f
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
        val t = System.currentTimeMillis()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                strokeBuilder = Ink.Stroke.builder()
                strokeBuilder.addPoint(Ink.Point.create(x, y, t))
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                strokeBuilder!!.addPoint(Ink.Point.create(x, y, t))
            }
            MotionEvent.ACTION_UP -> {
                strokeBuilder.addPoint(Ink.Point.create(x, y, t))
                inkBuilder.addStroke(strokeBuilder.build())
            }
            else -> return false
        }
        postInvalidate()
        return true
    }

    fun clear() {
        path.reset()
        postInvalidate()
    }

    fun getContent(callback: (String) -> Unit) {
        var modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("ja")
        var model: DigitalInkRecognitionModel =
            DigitalInkRecognitionModel.builder(modelIdentifier!!).build()
        remoteModelManager.isModelDownloaded(model).addOnSuccessListener {
            getContentInternal(model, callback)
        } .addOnFailureListener {
            remoteModelManager.download(model, DownloadConditions.Builder().build())
                .addOnSuccessListener {
                    getContentInternal(model, callback)
                }
                .addOnFailureListener { e: Exception ->
                    callback(e.message!!)
                }
        }

    }

    fun getContentInternal(model : DigitalInkRecognitionModel, callback: (String) -> Unit) {
        var recognizer = DigitalInkRecognition.getClient(DigitalInkRecognizerOptions.builder(model).build())
        recognizer.recognize(ink)
            .addOnSuccessListener { result: RecognitionResult ->
                callback(result.candidates[0].text)
            }
            .addOnFailureListener { e: Exception ->
                callback(e.message!!)
            }
    }

    val paint: Paint
    var path: Path
    var inkBuilder = Ink.builder()
    lateinit var strokeBuilder: Ink.Stroke.Builder
    val ink = inkBuilder.build()
    val remoteModelManager = RemoteModelManager.getInstance()
}