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
        val btm = findViewById<DrawingView>(R.id.viewDraw).getContent()
    }

    fun clear(view: View) {
        findViewById<DrawingView>(R.id.viewDraw).clear()
    }

    lateinit var hiraganaDrawInfo: Array<KanaDrawInfo>
    lateinit var katakanaDrawInfo: Array<KanaDrawInfo>
}