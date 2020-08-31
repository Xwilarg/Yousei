package com.xwilarg.yousei.quizz;

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.xwilarg.yousei.*
import com.xwilarg.yousei.learning.*

open class QuizzCommon : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz_choices)
    }

    fun preload() {
        // Load the right file in the right learning class given the intent from the main menu
        var intentValue = intent.getSerializableExtra("LEARNING_TYPE")
        var jlptValue = intent.getSerializableExtra("JLPT") as Int
        learning = when (intentValue) {
            LearningType.KANJI_TRANSLATE -> {
                LearningKanjiTranslate(
                    this.resources.openRawResource(
                        getKanjiJlpt(jlptValue)
                    ).bufferedReader().use { it.readText() })
            }
            LearningType.VOCABULARY_TRANSLATE -> {
                LearningVocabularyTranslate(
                    this.resources.openRawResource(
                        getVocabularyJlpt(jlptValue)
                    ).bufferedReader().use { it.readText() })
            }
            LearningType.HIRAGANA -> {
                LearningHiragana(
                    this.resources.openRawResource(
                        R.raw.hiragana
                    ).bufferedReader().use { it.readText() })
            }
            LearningType.KATAKANA -> {
                LearningHiragana(
                    this.resources.openRawResource(
                        R.raw.katakana
                    ).bufferedReader().use { it.readText() })
            }
            LearningType.SENTENCE -> {
                SentenceLearning(this.resources.openRawResource(
                    R.raw.sentences
                ).bufferedReader().use { it.readText() },
                    this.resources.openRawResource(R.raw.particles)
                        .bufferedReader().use { it.readText() },
                    this.resources.openRawResource(R.raw.hiragana)
                        .bufferedReader().use { it.readText() })
            }
            LearningType.KANJI_READ -> {
                LearningKanjiRead(this.resources.openRawResource(
                    getKanjiJlpt(jlptValue)
                ).bufferedReader().use { it.readText() },
                    this.resources.openRawResource(R.raw.hiragana)
                        .bufferedReader().use { it.readText() },
                    this.resources.openRawResource(R.raw.katakana)
                        .bufferedReader().use { it.readText() })
            }
            LearningType.VOCABULARY_READ -> {
                LearningVocabularyRead(this.resources.openRawResource(
                    getVocabularyJlpt(jlptValue)
                ).bufferedReader().use { it.readText() },
                    this.resources.openRawResource(R.raw.hiragana)
                        .bufferedReader().use { it.readText() })
            }
            else -> {
                LearningKanjiConvert(this.resources.openRawResource(
                        getVocabularyJlpt(jlptValue)
                    ).bufferedReader().use { it.readText() })
            }
        }
        // Since sentences are big the text size
        if (intentValue == LearningType.SENTENCE) {
            findViewById<TextView>(R.id.textQuizz).textSize = 30f
            findViewById<TextView>(R.id.textQuizzHelp).textSize = 20f
        }
        loadQuestion()
    }

    fun getKanjiJlpt(value: Int) : Int {
        return when {
            value == 1 -> R.raw.kanji_jlpt1
            value == 2 -> R.raw.kanji_jlpt2
            value == 3 -> R.raw.kanji_jlpt3
            value == 4 -> R.raw.kanji_jlpt4
            value == 5 -> R.raw.kanji_jlpt5
            else -> throw Exception("Invalid value $value")
        }
    }

    fun getVocabularyJlpt(value: Int) : Int {
        return when {
            value == 1 -> R.raw.vocabulary_jlpt1
            value == 2 -> R.raw.vocabulary_jlpt2
            value == 3 -> R.raw.vocabulary_jlpt3
            value == 4 -> R.raw.vocabulary_jlpt4
            value == 5 -> R.raw.vocabulary_jlpt5
            else -> throw Exception("Invalid value $value")
        }
    }

    open fun checkAnswer(myAnswer: List<String>) {
        var corr: IsCorrect? = null
        for(a in myAnswer) {
            var answer = learning.checkAnswer(a)
            if (corr == null || answer.first > corr)
            {
                if (answer.first == IsCorrect.YES) {
                    checkAnswerInternal(a, answer)
                    return
                }
                corr = answer.first
            }
        }
        checkAnswerInternal(myAnswer[0], learning.checkAnswer(myAnswer[0]))
    }

    open fun checkAnswer(myAnswer: String) {
        checkAnswerInternal(myAnswer, learning.checkAnswer(myAnswer))
    }

    fun checkAnswerInternal(myAnswer: String, answer: Pair<IsCorrect, String>) {
        // Display your answer and the correct one
        findViewById<TextView>(R.id.textLastKanji).text = learning.getCurrent()
        findViewById<TextView>(R.id.textAnswerYouTitle).text = "Your answer"
        findViewById<TextView>(R.id.textAnswerHimTitle).text = "Right answer"

        findViewById<TextView>(R.id.textAnswerYou).text = learning.getAnswer(myAnswer)

        // If the answer is right, display green
        // If it's wrong, red
        // If it's partially correct, yellow
        findViewById<ConstraintLayout>(R.id.ConstraintLayoutAnswer).setBackgroundColor(
            when (answer.first) {
                IsCorrect.YES -> {
                    Color.rgb(200, 255, 200)
                }
                IsCorrect.PARTIAL -> {
                    Color.rgb(255, 255, 200)
                }
                else -> {
                    Color.rgb(255, 200, 200)
                }
            }
        )
        // Display in the answer the last question
        findViewById<TextView>(R.id.textAnswerHim).text = answer.second
        loadQuestion()
    }

    fun loadQuestion() {
        val question = learning.getQuestion()
        // textQuizz is the question and textQuizzHelp is a small text under it displayed as a help
        findViewById<TextView>(R.id.textQuizz).text = question.first
        findViewById<TextView>(R.id.textQuizzHelp).text = question.second
        loadQuestionAfter()
    }

    open fun loadQuestionAfter() { } // virtual function called after the question was loaded

    lateinit var learning: ILearning // learning is the "learning type" (is the user studying kanjis, hiraganas, etc...)
}