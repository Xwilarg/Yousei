package com.xwilarg.yousei.learning

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xwilarg.yousei.quizz.IsCorrect
import kotlin.random.Random

class LearningComplete : ILearning {

    constructor(content: String) {
        sentences = Gson().fromJson(content, Array<SentenceInfo>::class.java)
    }

    override fun getQuestion() : Pair<String, String> {
        currentSentence = sentences[Random.nextInt(0, sentences.size)]
        fullSentence = prepareSentence()
        return Pair(currentSentence.meaning, "")
    }

    fun prepareSentence() : String {
        fullSentence = ""
        var rParticle = Random.nextInt(0, currentSentence.particleCount) // rParticle contains which particle must be guessed
        for (word in currentSentence.words) {
            if (word.isParticle) {
                if (rParticle == 0) {
                    rParticle--
                    fullSentence += "??? "
                    continue // We continue here so the word is not added to the sentence
                }
                rParticle--
            }
            fullSentence += word.word + " "
        }
        return fullSentence.substring(0, fullSentence.length - 1) // We remove the last space
    }

    override fun checkAnswer(myAnswer: String) : Pair<IsCorrect, String> {
        return Pair(if (fullSentence == myAnswer) {
            IsCorrect.YES
        } else {
            IsCorrect.NO
        }, fullSentence)
    }

    override fun getCurrent(): String {
        return "" // getCurrent is used to display the question in the answer but in this mode, the question is too big to fit
    }

    override fun getRandomChoices(): ArrayList<String> {
        throw Exception("getRandomChoices should not be called for LearningComplete")
    }

    override fun getAnswer(answer: String): String {
        return fullSentence
    }

    var sentences: Array<SentenceInfo>
    lateinit var fullSentence: String
    lateinit var currentSentence: SentenceInfo
}