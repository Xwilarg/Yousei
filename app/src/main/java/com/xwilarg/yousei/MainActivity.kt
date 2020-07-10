package com.xwilarg.yousei

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    
    fun startGameFreeHiragana(view: View) {
        var intent = Intent(this, QuizzNormalActivity::class.java)
        intent.putExtra("LEARNING_TYPE", LearningType.HIRAGANA)
        startActivity(intent)
    }

    fun startGameChoicesHiragana(view: View) {
        var intent = Intent(this, QuizzChoicesActivity::class.java)
        intent.putExtra("LEARNING_TYPE", LearningType.HIRAGANA)
        startActivity(intent)
    }

    fun startGameFreeKanji(view: View) {
        var intent = Intent(this, QuizzNormalActivity::class.java)
        intent.putExtra("LEARNING_TYPE", LearningType.KANJI)
        startActivity(intent)
    }

    fun startGameChoicesKanji(view: View) {
        var intent = Intent(this, QuizzChoicesActivity::class.java)
        intent.putExtra("LEARNING_TYPE", LearningType.KANJI)
        startActivity(intent)
    }
}