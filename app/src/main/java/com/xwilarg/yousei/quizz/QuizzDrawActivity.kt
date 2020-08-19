package com.xwilarg.yousei.quizz

import android.os.Bundle
import android.view.View
import com.xwilarg.yousei.DrawingView
import com.xwilarg.yousei.R

class QuizzDrawActivity : QuizzCommon() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz_draw)
        preload()
    }

    fun answer(view: View) {
        findViewById<DrawingView>(R.id.viewDraw).getContent({
            msg -> checkAnswer(msg)
        })
    }

    fun clear(view: View) {
        findViewById<DrawingView>(R.id.viewDraw).clear()
    }
}