package eu.zirk.yousei.quizz

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import eu.zirk.yousei.DrawingView
import eu.zirk.yousei.R

class QuizzDrawActivity : QuizzCommon() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz_draw)
        preloadWithDownload()
    }

    fun answer(view: View) {
        findViewById<DrawingView>(R.id.viewDraw).getContent {
            msg -> if (msg == null) {
                checkAnswer("")
            } else {
                checkAnswer(msg)
            }
            findViewById<DrawingView>(R.id.viewDraw).clear()
        }
    }

    fun clear(view: View) {
        findViewById<DrawingView>(R.id.viewDraw).clear()
    }
}