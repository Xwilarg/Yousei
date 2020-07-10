package com.xwilarg.yousei;

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

open class QuizzCommon : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz_choices)
    }

    fun preload() {
        learning = if (intent.getSerializableExtra("LEARNING_TYPE") == LearningType.KANJI) {
            KanjiLearning(this.resources.openRawResource(R.raw.jlpt5).bufferedReader().use { it.readText() })
        } else {
            HiraganaLearning(this.resources.openRawResource(R.raw.hiragana).bufferedReader().use { it.readText() })
        }
        loadQuestion()
    }

    open fun checkAnswer(myAnswer: String) {
        findViewById<TextView>(R.id.textLastKanji).text = learning.getCurrent()
        findViewById<TextView>(R.id.textAnswerYouTitle).text = "Your answer"
        findViewById<TextView>(R.id.textAnswerHimTitle).text = "Right answer"
        findViewById<TextView>(R.id.textAnswerYou).text = myAnswer

        val answer = learning.checkAnswer(myAnswer)

        findViewById<ConstraintLayout>(R.id.ConstraintLayoutAnswer).setBackgroundColor(if (answer.first == IsCorrect.YES) {
            Color.rgb(200, 255, 200)
        } else if (answer.first == IsCorrect.PARTIAL) {
            Color.rgb(255, 255, 200)
        } else {
            Color.rgb(255, 200, 200)
        })
        findViewById<TextView>(R.id.textAnswerHim).text = answer.second
        loadQuestion()
    }

    fun loadQuestion() {
        val question = learning.getQuestion()
        findViewById<TextView>(R.id.textQuizz).text = question.first
        findViewById<TextView>(R.id.textQuizzHelp).text = question.second
        loadQuestionAfter()
    }

    open fun loadQuestionAfter() { }

    lateinit var learning: ILearning
}