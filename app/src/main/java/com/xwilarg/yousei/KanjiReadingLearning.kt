package com.xwilarg.yousei

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.random.Random

class KanjiReadingLearning : ILearning {

    constructor(content: String, contentHiraganas: String, contentKatakanas: String) {
        kanjis = Gson().fromJson(content, Array<KanjiInfo>::class.java)
        hiraganas = Gson().fromJson(contentHiraganas, object : TypeToken<Map<String, String>>() {}.type)
        hiraganas = hiraganas.entries.associateBy({ it.value }) {it.key}
        katakanas = Gson().fromJson(contentKatakanas, object : TypeToken<Map<String, String>>() {}.type)
        katakanas = katakanas.entries.associateBy({ it.value }) {it.key}
    }

    override fun getQuestion() : Pair<String, String> {
        currentKanji = kanjis[Random.nextInt(0, kanjis.size)]
        return Pair(currentKanji.kanji, currentKanji.meaning[0])
    }

    override fun checkAnswer(myAnswer: String) : Pair<IsCorrect, String> {
        var isCorrect = false
        var closestAnswer : String? = null
        if (!myAnswer.isNullOrBlank()) {
            // We need to check if the answer is correct, looking in the onyomi and the kunyomi
            var kanaAnswer = UtilsLearning.convertStringHiragana(myAnswer, hiraganas)
            for (m in currentKanji.onyomi) {
                if (kanaAnswer == m) {
                    return Pair(IsCorrect.YES, m)
                }
                if (closestAnswer == null && (kanaAnswer.contains(m) || m.contains(kanaAnswer))) {
                    closestAnswer = m
                }
            }
            kanaAnswer = UtilsLearning.convertStringKatakana(kanaAnswer, katakanas)
            for (m in currentKanji.kunyomi) {
                if (kanaAnswer == m) {
                    return Pair(IsCorrect.YES, m)
                }
                if (closestAnswer == null && (kanaAnswer.contains(m) || m.contains(kanaAnswer))) {
                    closestAnswer = m
                }
            }
        }
        if (closestAnswer != null) {
            return Pair(IsCorrect.PARTIAL, closestAnswer)
        }
        return Pair(IsCorrect.NO, kanjiInfoToKana(currentKanji))
    }

    override fun getCurrent(): String {
        return currentKanji.kanji
    }

    override fun getRandomChoices(): ArrayList<String> {
        var choices = arrayListOf<String>()
        var goodAnswer = kanjiInfoToKana(currentKanji)
        choices.add(goodAnswer)
        var dotValue = ""
        // If the word is like "い.く" we add ".く" to all the answers so the player can't guess just with that
        if (goodAnswer.contains(".")) {
            dotValue = "." + goodAnswer.split(".")[1]
        }
        while (choices.size < 4) {
            var randomChoice = kanjis[Random.nextInt(0, kanjis.size)]
            var kana = kanjiInfoToKana(randomChoice)
            if (dotValue != "") {
                if (kana.contains(".") && kana.split(".")[1] == dotValue) { // If the bad answer already contains a . and it's the same ending as the good answer
                    // Do nothing
                } else if (kana.contains(".")) { // The bad answer have a . but not the same ending
                    kana = kana.split(".")[0] + dotValue // We take the base word and add our own ending
                } else {
                    kana += dotValue // We just add our ending
                }
            } else if (kana.contains(".")) { // The good answer doesn't contains a . but the bad answer does
                kana = kana.split(".")[0] // We just remove the ending
            }
            if (!choices.contains(kana)) {
                choices.add(kana)
            }
        }
        choices.shuffle()
        return choices
    }

    fun kanjiInfoToKana(kanji: KanjiInfo): String {
        return if (kanji.kunyomi.isEmpty()) {
            kanji.onyomi?.get(0)
        } else {
            kanji.kunyomi?.get(0)
        }
    }

    override fun getAnswer(answer: String): String {
        return UtilsLearning.convertStringHiragana(answer, hiraganas)
    }

    var kanjis: Array<KanjiInfo>
    lateinit var currentKanji: KanjiInfo

    var hiraganas: Map<String, String>
    var katakanas: Map<String, String>
}