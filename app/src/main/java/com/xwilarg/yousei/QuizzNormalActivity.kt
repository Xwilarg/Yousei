package com.xwilarg.yousei

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText

class QuizzNormalActivity : QuizzCommon() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz_normal)
        preload()
    }

    fun answer(view: View) {
        checkAnswer(findViewById<EditText>(R.id.editTextAnswer).text.toString().toLowerCase())
    }

    override fun loadQuestionAfter() {
        findViewById<EditText>(R.id.editTextAnswer).text = Editable.Factory.getInstance().newEditable("")
    }
}