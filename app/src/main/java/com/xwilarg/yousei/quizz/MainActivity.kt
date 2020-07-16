package com.xwilarg.yousei.quizz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import com.xwilarg.yousei.R
import com.xwilarg.yousei.learning.LearningType

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        intent.putExtra("LEARNING_TYPE",
            LearningType.HIRAGANA
        )
        startActivity(intent)
    }

    fun startGameKatakana(view: View) {
        var intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE",
            LearningType.KATAKANA
        )
        startActivity(intent)
    }

    fun startGameKanji(view: View) {
        var intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE",
            LearningType.KANJI
        )
        startActivity(intent)
    }

    fun startGameKanjiReading(view: View) {
        var intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE",
            LearningType.KANJI_READING
        )
        startActivity(intent)
    }

    fun startGameVocabulary(view: View) {
        var intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE",
            LearningType.VOCABULARY
        )
        startActivity(intent)
    }

    fun startGameVocabularyReading(view: View) {
        var intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE",
            LearningType.VOCABULARY_READING
        )
        startActivity(intent)
    }

    fun startGameSentence(view: View) {
        var intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE",
            LearningType.SENTENCE
        )
        startActivity(intent)
    }

    fun getQuizzType() : Class<*> {
        return if (findViewById<RadioButton>(R.id.radioChoices).isChecked) {
            QuizzChoicesActivity::class.java
        } else if (findViewById<RadioButton>(R.id.radioFreeText).isChecked) {
            QuizzNormalActivity::class.java
        } else {
            QuizzDrawActivity::class.java
        }
    }
}