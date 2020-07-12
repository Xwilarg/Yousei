package com.xwilarg.yousei

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.random.Random

class VocabularyReadingLearning : ILearning {

    constructor(content: String, contentHiraganas: String) {
        words = Gson().fromJson(content, Array<VocabularyInfo>::class.java)
        hiraganas = Gson().fromJson(contentHiraganas, object : TypeToken<Map<String, String>>() {}.type)
        hiraganas = hiraganas.entries.associateBy({ it.value }) {it.key}
    }

    override fun getQuestion() : Pair<String, String> {
        currentWord = words[Random.nextInt(0, words.size)]
        return Pair(currentWord.word, currentWord.meaning[0])
    }

    override fun checkAnswer(myAnswer: String) : Pair<IsCorrect, String> {
        return Pair(if (myAnswer == currentWord.reading) {
            IsCorrect.YES
        } else {
            IsCorrect.NO
        }, currentWord.reading)
    }

    override fun getCurrent(): String {
        return currentWord.word
    }

    override fun getRandomChoices(): ArrayList<String> {
        var choices = arrayListOf<String>()
        choices.add(currentWord.reading)
        while (choices.size < 4) {
            var randomChoice = words[Random.nextInt(0, words.size)].reading
            if (!choices.contains(randomChoice)) {
                choices.add(randomChoice)
            }
        }
        choices.shuffle()
        return choices
    }

    override fun getAnswer(answer: String): String {
        return UtilsLearning.convertStringHiragana(answer, hiraganas)
    }

    var words: Array<VocabularyInfo>
    lateinit var currentWord: VocabularyInfo

    var hiraganas: Map<String, String>
}