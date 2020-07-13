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
        learning = when (intentValue) {
            LearningType.KANJI -> {
                KanjiLearning(
                    this.resources.openRawResource(
                        R.raw.kanji_jlpt5
                    ).bufferedReader().use { it.readText() })
            }
            LearningType.VOCABULARY -> {
                VocabularyLearning(
                    this.resources.openRawResource(
                        R.raw.vocabulary_jlpt5
                    ).bufferedReader().use { it.readText() })
            }
            LearningType.HIRAGANA -> {
                HiraganaLearning(
                    this.resources.openRawResource(
                        R.raw.hiragana
                    ).bufferedReader().use { it.readText() })
            }
            LearningType.KATAKANA -> {
                HiraganaLearning(
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
            LearningType.KANJI_READING -> {
                KanjiReadingLearning(this.resources.openRawResource(
                    R.raw.kanji_jlpt5
                ).bufferedReader().use { it.readText() },
                    this.resources.openRawResource(R.raw.hiragana)
                        .bufferedReader().use { it.readText() },
                    this.resources.openRawResource(R.raw.katakana)
                        .bufferedReader().use { it.readText() })
            }
            else -> {
                VocabularyReadingLearning(this.resources.openRawResource(
                    R.raw.vocabulary_jlpt5
                ).bufferedReader().use { it.readText() },
                    this.resources.openRawResource(R.raw.hiragana)
                        .bufferedReader().use { it.readText() })
            }
        }
        // Since sentences are big the text size
        if (intentValue == LearningType.SENTENCE) {
            findViewById<TextView>(R.id.textQuizz).textSize = 30f
            findViewById<TextView>(R.id.textQuizzHelp).textSize = 20f
        }
        loadQuestion()
    }
    open fun checkAnswer(myAnswer: String) {
        // Display your answer and the correct one
        findViewById<TextView>(R.id.textLastKanji).text = learning.getCurrent()
        findViewById<TextView>(R.id.textAnswerYouTitle).text = "Your answer"
        findViewById<TextView>(R.id.textAnswerHimTitle).text = "Right answer"

        // These 2 lines must be called in this order because of KanjiReadingLearning initialising some stuffs in checkAnswer for getAnswer
        val answer = learning.checkAnswer(myAnswer)
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