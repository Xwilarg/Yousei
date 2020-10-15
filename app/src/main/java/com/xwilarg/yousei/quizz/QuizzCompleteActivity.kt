package com.xwilarg.yousei.quizz

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.xwilarg.yousei.R


class QuizzCompleteActivity : QuizzCommon() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz_complete)
        preload()
    }

    fun answer(view: View) {
        checkAnswer(findViewById<EditText>(R.id.answer).text.toString().toLowerCase())
    }

    override fun loadQuestionAfter() {
        findViewById<EditText>(R.id.answer).setText("")
        val answer = learning.getCurrent()

        for ((i, word) in answer.split(' ').withIndex()) {
            val layout = findViewById<View>(R.id.choices) as LinearLayout
            val button = Button(this)
            button.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            button.text = "Button $i"

            layout.addView(button)

        }
    }
}