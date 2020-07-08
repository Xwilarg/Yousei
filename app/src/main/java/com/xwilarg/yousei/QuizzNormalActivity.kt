package com.xwilarg.yousei

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlin.random.Random


class QuizzNormalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz_normal)
        val content = this.resources.openRawResource(R.raw.jlpt5).bufferedReader().use { it.readText() }
        kanjis = Gson().fromJson(content, Array<KanjiInfo>::class.java)
        loadQuestion()
    }

    fun answer(view: View) {
        loadQuestion()
    }

    fun loadQuestion() {
        val randomKanji = kanjis[Random.nextInt(0, kanjis.size)]
        findViewById<TextView>(R.id.textQuizz).text = randomKanji.kanji
        findViewById<TextView>(R.id.textQuizzHelp).text = if (randomKanji.kunyomi == null) {
            randomKanji.onyomi?.get(0)
        } else {
            randomKanji.kunyomi?.get(0)
        }
    }

    lateinit var kanjis: Array<KanjiInfo>
}

data class KanjiInfo (
    val kanji: String,
    val meaning: Array<String>,
    val onyomi: Array<String>,
    val kunyomi: Array<String>,
    val imageId: String
)