package com.xwilarg.yousei

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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
                strokeBuilder.addPoint(Ink.Point.create(x, y, t))
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
        inkBuilder = Ink.builder()
        postInvalidate()
    }

    fun getContent(callback: (List<String>?) -> Unit) {
        if (path.isEmpty) {
            callback(null)
        } else {
            var modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("ja")
            var model: DigitalInkRecognitionModel =
                DigitalInkRecognitionModel.builder(modelIdentifier!!).build()
            remoteModelManager.isModelDownloaded(model).addOnSuccessListener { res: Boolean ->
                if (res) {
                    getContentInternal(model, callback)
                } else {
                    remoteModelManager.download(model, DownloadConditions.Builder().build())
                        .addOnSuccessListener {
                            getContentInternal(model, callback)
                        }
                        .addOnFailureListener { e: Exception ->
                            AlertDialog.Builder(context).setMessage("An error occurred while processing the request: " + e.message!!).setPositiveButton("OK") { dial: DialogInterface, _: Int -> dial.dismiss()}.create().show()
                            callback(null)
                        }
                }
            } .addOnFailureListener { e: Exception ->
                AlertDialog.Builder(context).setMessage("An error occurred while processing the request: " + e.message!!).setPositiveButton("OK") { dial: DialogInterface, _: Int -> dial.dismiss()}.create().show()
                callback(null)
            }
        }
    }

    private fun getContentInternal(model : DigitalInkRecognitionModel, callback: (List<String>?) -> Unit) {
        var recognizer = DigitalInkRecognition.getClient(DigitalInkRecognizerOptions.builder(model).build())
        val ink = inkBuilder.build()
        recognizer.recognize(ink)
            .addOnSuccessListener { result: RecognitionResult ->
                if (BuildConfig.DEBUG) {
                    for (res in result.candidates) {
                        Log.d("res", res.text)
                    }
                }
                callback(result.candidates.map { it.text })
            }
            .addOnFailureListener { e: Exception ->
                AlertDialog.Builder(context).setMessage("An error occurred while processing the request: " + e.message!!).setPositiveButton("OK") { dial: DialogInterface, _: Int -> dial.dismiss()}.create().show()
                callback(null)
            }
    }

    val paint: Paint
    var path: Path
    var inkBuilder = Ink.builder()
    lateinit var strokeBuilder: Ink.Stroke.Builder
    val remoteModelManager = RemoteModelManager.getInstance()
}