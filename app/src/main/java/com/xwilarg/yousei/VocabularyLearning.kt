package com.xwilarg.yousei;

import com.google.gson.Gson
import kotlin.random.Random

class VocabularyLearning : ILearning {

    constructor(content: String) {
        words = Gson().fromJson(content, Array<VocabularyInfo>::class.java)
    }

    override fun getQuestion() : Pair<String, String> {
        currentWord = words[Random.nextInt(0, words.size)]
        return Pair(currentWord.word, currentWord.reading)
    }

    override fun checkAnswer(myAnswer: String) : Pair<IsCorrect, String> {
        var isCorrect = false
        var closestAnswer : String? = null
        if (!myAnswer.isNullOrBlank()) {
            for (m in currentWord   .meaning) {
                if (myAnswer == m) {
                    return Pair(IsCorrect.YES, m)
                }
                if (closestAnswer == null && (myAnswer.contains(m) || m.contains(myAnswer))) {
                    closestAnswer = m
                }
            }
        }
        if (closestAnswer != null) {
            return Pair(IsCorrect.PARTIAL, closestAnswer)
        }
        return Pair(IsCorrect.NO, currentWord.meaning[0])
    }

    override fun getCurrent(): String {
        return currentWord.word
    }

    override fun getRandomChoices(): ArrayList<String> {
        var choices = arrayListOf<String>()
        choices.add(currentWord.meaning[0])
        while (choices.size < 4) {
            var randomChoice = words[Random.nextInt(0, words.size)].meaning[0]
            if (!choices.contains(randomChoice)) {
                choices.add(randomChoice)
            }
        }
        choices.shuffle()
        return choices
    }

    var words: Array<VocabularyInfo>
    lateinit var currentWord: VocabularyInfo
}
