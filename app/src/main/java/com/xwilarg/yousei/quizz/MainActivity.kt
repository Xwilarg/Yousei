package com.xwilarg.yousei.quizz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.core.widget.addTextChangedListener
import com.xwilarg.yousei.R
import com.xwilarg.yousei.learning.LearningType

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<EditText>(R.id.jlptValue).addTextChangedListener { text: Editable? -> run {
                if (text != null && text.isNotEmpty()) {
                    val value = text.toString().toInt()
                    if (value < 1 || value > 5)
                        findViewById<EditText>(R.id.jlptValue).setText(jlptValue.toString())
                    else
                        jlptValue = value
                }
            }
        }
    }

    fun onRadioGroupClick(view: View) {
        val isDraw = findViewById<RadioButton>(R.id.radioDraw).isChecked
        findViewById<Button>(R.id.buttonQuizzHiragana).isEnabled = !isDraw
        findViewById<Button>(R.id.buttonQuizzKatakana).isEnabled = !isDraw
        findViewById<Button>(R.id.buttonQuizzKanji).isEnabled = !isDraw
        findViewById<Button>(R.id.buttonQuizzVocabulary).isEnabled = !isDraw
    }
    
    fun startGameHiragana(view: View) {
        val intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.HIRAGANA)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameKatakana(view: View) {
        val intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.KATAKANA)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameKanji(view: View) {
        val intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.KANJI_TRANSLATE)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameKanjiReading(view: View) {
        val intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.KANJI_READ)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameKanjiConvert(view: View) {
        val intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.KANJI_CONVERT)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameVocabulary(view: View) {
        val intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.VOCABULARY_TRANSLATE)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameVocabularyReading(view: View) {
        val intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.VOCABULARY_READ)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameVocabularyConvert(view: View) {
        val intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.VOCABULARY_CONVERT)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameSentence(view: View) {
        val intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.SENTENCE)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    private fun getQuizzType() : Class<*> {
        return when {
            findViewById<RadioButton>(R.id.radioChoices).isChecked -> {
                QuizzChoicesActivity::class.java
            }
            findViewById<RadioButton>(R.id.radioFreeText).isChecked -> {
                QuizzNormalActivity::class.java
            }
            else -> {
                QuizzDrawActivity::class.java
            }
        }
    }

    private var jlptValue = 5
}