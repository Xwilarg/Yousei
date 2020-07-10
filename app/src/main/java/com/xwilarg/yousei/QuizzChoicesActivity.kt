package com.xwilarg.yousei

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import kotlin.random.Random

class QuizzChoicesActivity : QuizzCommon() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz_choices)
        preload()
    }

    fun answer(id: Int) {
        checkAnswer(findViewById<Button>(id).text.toString().toLowerCase())
    }

    fun answer1(view: View) {
        answer(R.id.buttonAnswer1)
    }

    fun answer2(view: View) {
        answer(R.id.buttonAnswer2)
    }

    fun answer3(view: View) {
        answer(R.id.buttonAnswer3)
    }

    fun answer4(view: View) {
        answer(R.id.buttonAnswer4)
    }

    override fun loadQuestionAfter() {
        var choices = arrayListOf<String>()
        choices.add(currentKanji.meaning[0])
        while (choices.size < 4) {
            var randomChoice = kanjis[Random.nextInt(0, kanjis.size)].meaning[0]
            if (!choices.contains(randomChoice)) {
                choices.add(randomChoice)
            }
        }
        choices.shuffle()
        findViewById<Button>(R.id.buttonAnswer1).text = choices[0]
        findViewById<Button>(R.id.buttonAnswer2).text = choices[1]
        findViewById<Button>(R.id.buttonAnswer3).text = choices[2]
        findViewById<Button>(R.id.buttonAnswer4).text = choices[3]
    }
}