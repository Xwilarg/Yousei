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
                    var value = text.toString().toIntOrNull()
                    if (value == null || value < 1 || value > 5)
                        findViewById<EditText>(R.id.jlptValue).setText(jlptValue.toString())
                    else
                        jlptValue = value
                }
            }
        }
    }

    fun onRadioGroupClick(view: View) {
        var isDraw = findViewById<RadioButton>(R.id.radioDraw).isChecked
        findViewById<Button>(R.id.buttonQuizzHiragana).isEnabled = !isDraw
        findViewById<Button>(R.id.buttonQuizzKatakana).isEnabled = !isDraw
        findViewById<Button>(R.id.buttonQuizzKanji).isEnabled = !isDraw
        findViewById<Button>(R.id.buttonQuizzVocabulary).isEnabled = !isDraw
    }
    
    fun startGameHiragana(view: View) {
        var intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.HIRAGANA)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameKatakana(view: View) {
        var intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.KATAKANA)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameKanji(view: View) {
        var intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.KANJI)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameKanjiReading(view: View) {
        var intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.KANJI_READING)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameVocabulary(view: View) {
        var intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.VOCABULARY)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameVocabularyReading(view: View) {
        var intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.VOCABULARY_READING)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameSentence(view: View) {
        var intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.SENTENCE)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun getQuizzType() : Class<*> {
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