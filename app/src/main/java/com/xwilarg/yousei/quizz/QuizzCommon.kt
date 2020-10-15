package com.xwilarg.yousei.quizz;

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.xwilarg.yousei.MainActivity
import com.xwilarg.yousei.R
import com.xwilarg.yousei.learning.*

open class QuizzCommon : AppCompatActivity() {

    fun preloadWithDownload() { // Download OCR data from draw quizz
        var modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("ja")
        var model: DigitalInkRecognitionModel =
            DigitalInkRecognitionModel.builder(modelIdentifier!!).build()
        val remoteModelManager = RemoteModelManager.getInstance()
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Updating OCR data, please wait...")
        builder.setPositiveButton("Cancel") { _: DialogInterface, _: Int ->
            startActivity(Intent(this, MainActivity::class.java))
        }
        val popup = builder.create()
        popup.setCanceledOnTouchOutside(false)
        popup.show()
        remoteModelManager.isModelDownloaded(model).addOnSuccessListener { res: Boolean ->
            if (res) {
                popup.dismiss()
                preload()
            } else {
                remoteModelManager.download(model, DownloadConditions.Builder().build())
                    .addOnSuccessListener {
                        popup.dismiss()
                        preload()
                    }
                    .addOnFailureListener { e: Exception ->
                        popup.dismiss()
                        builder.setMessage("An error occurred while downloading OCR data: " + e.message!!)
                        builder.setPositiveButton("OK") { _: DialogInterface, _: Int ->
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        val p = builder.create()
                        p.setCanceledOnTouchOutside(false)
                        p.show()
                    }
            }
        }.addOnFailureListener { e: Exception ->
            popup.hide()
            builder.setMessage("An error occurred while checking OCR data: " + e.message!!)
            builder.setPositiveButton("OK") { _: DialogInterface, _: Int ->
                startActivity(Intent(this, MainActivity::class.java))
            }
            val p = builder.create()
            p.setCanceledOnTouchOutside(false)
            p.show()
        }
    }

    fun preload()
    {
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
                LearningSentence(this.resources.openRawResource(
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
            LearningType.KANJI_CONVERT -> {
                LearningKanjiConvert(this.resources.openRawResource(
                    getKanjiJlpt(jlptValue)
                ).bufferedReader().use { it.readText() })
            }
            LearningType.COMPLETE -> {
                LearningComplete(this.resources.openRawResource(
                    R.raw.sentences
                ).bufferedReader().use { it.readText() })
            }
            else -> {
                LearningVocabularyConvert(this.resources.openRawResource(
                    getVocabularyJlpt(jlptValue)
                ).bufferedReader().use { it.readText() })
            }
        }
        // Since sentences are big, we reduce the text size
        if (intentValue == LearningType.SENTENCE || intentValue == LearningType.COMPLETE) {
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