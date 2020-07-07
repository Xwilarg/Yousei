package com.xwilarg.yousei

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class QuizzNormalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz_normal)
    }

    fun answer(view: View) {
        loadQuestion()
    }

    fun loadQuestion() {

    }
}