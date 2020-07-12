package com.xwilarg.yousei

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    
    fun startGameHiragana(view: View) {
        var intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.HIRAGANA)
        startActivity(intent)
    }

    fun startGameKatakana(view: View) {
        var intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.KATAKANA)
        startActivity(intent)
    }

    fun startGameKanji(view: View) {
        var intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.KANJI)
        startActivity(intent)
    }

    fun startGameVocabulary(view: View) {
        var intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.VOCABULARY)
        startActivity(intent)
    }

    fun startGameSentence(view: View) {
        var intent = Intent(this, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.SENTENCE)
        startActivity(intent)
    }

    fun getQuizzType() : Class<*> {
        return if (findViewById<RadioButton>(R.id.radioChoices).isChecked) {
            QuizzChoicesActivity::class.java
        } else {
            QuizzNormalActivity::class.java
        }
    }
}