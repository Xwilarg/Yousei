package com.xwilarg.yousei

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
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
        findViewById<TextView>(R.id.textLastKanji).text = currentKanji.kanji
        findViewById<TextView>(R.id.textAnswerYouTitle).text = "Your answer"
        findViewById<TextView>(R.id.textAnswerHimTitle).text = "Right answer"
        findViewById<TextView>(R.id.textAnswerYou).text = findViewById<EditText>(R.id.editTextAnswer).text
        findViewById<TextView>(R.id.textAnswerHim).text = currentKanji.meaning[0]
        loadQuestion()
    }

    fun loadQuestion() {
        currentKanji = kanjis[Random.nextInt(0, kanjis.size)]
        findViewById<TextView>(R.id.textQuizz).text = currentKanji.kanji
        findViewById<TextView>(R.id.textQuizzHelp).text = if (currentKanji.kunyomi.isEmpty()) {
            currentKanji.onyomi?.get(0)
        } else {
            currentKanji.kunyomi?.get(0)
        }
        findViewById<EditText>(R.id.editTextAnswer).text = Editable.Factory.getInstance().newEditable("")
    }

    lateinit var kanjis: Array<KanjiInfo>
    lateinit var currentKanji: KanjiInfo
}

data class KanjiInfo (
    val kanji: String,
    val meaning: Array<String>,
    val onyomi: Array<String>,
    val kunyomi: Array<String>,
    val imageId: String
)