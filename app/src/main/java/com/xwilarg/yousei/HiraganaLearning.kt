package com.xwilarg.yousei

import com.google.gson.Gson
import kotlin.random.Random
import com.google.gson.reflect.TypeToken

class HiraganaLearning : ILearning {

    constructor(content: String) {
        hiraganas = Gson().fromJson(content, object : TypeToken<Map<String, String>>() {}.type)
    }

    override fun getQuestion() : Pair<String, String> {
        val key = hiraganas.keys.toTypedArray()[Random.nextInt(0, hiraganas.keys.size)]
        currentHiragana =  Pair(key, hiraganas[key]!!)
        return Pair(currentHiragana.first, "")
    }

    override fun checkAnswer(myAnswer: String) : Pair<IsCorrect, String> {
        return Pair(if (myAnswer == currentHiragana.second) {
            IsCorrect.YES
        } else {
            IsCorrect.NO
        }, currentHiragana.second)
    }

    override fun getCurrent(): String {
        return currentHiragana.first
    }

    override fun getRandomChoices(): ArrayList<String> {
        var choices = arrayListOf<String>()
        choices.add(currentHiragana.second)
        val keys = hiraganas.keys.toTypedArray()
        while (choices.size < 4) {
            val key = keys[Random.nextInt(0, keys.size)]
            var randomChoice = hiraganas[key]!!
            if (!choices.contains(randomChoice)) {
                choices.add(randomChoice)
            }
        }
        choices.shuffle()
        return choices
    }

    var hiraganas: Map<String, String>
    lateinit var currentHiragana: Pair<String, String>
}