package com.xwilarg.yousei.learning

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xwilarg.yousei.quizz.IsCorrect
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
        answerKana = myAnswer
        var closestAnswer : String? = null
        if (!myAnswer.isNullOrBlank()) {
            // We need to check if the answer is correct, looking in the onyomi and the kunyomi
            var kanaAnswer =
                UtilsLearning.convertStringKatakana(
                    myAnswer,
                    katakanas
                )
            for (m in currentKanji.onyomi) {
                if (kanaAnswer == m) {
                    answerKana = kanaAnswer
                    return Pair(IsCorrect.YES, m)
                }
                if (closestAnswer == null && (kanaAnswer.contains(m) || m.contains(kanaAnswer))) {
                    closestAnswer = m
                }
            }
            kanaAnswer = UtilsLearning.convertStringHiragana(
                myAnswer,
                hiraganas
            )
            for (mTmp in currentKanji.kunyomi) {
                var m = mTmp
                m = m.replace("-", "")
                if (m.contains(".")) {
                    if (kanaAnswer == m.replace(".", "")) {
                        return Pair(IsCorrect.YES, m)
                    }
                    m = m.split(".")[0]
                }
                if (kanaAnswer == m || kanaAnswer == mTmp) {
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

    private fun kanjiInfoToKana(kanji: KanjiInfo): String {
        return if (kanji.kunyomi.isEmpty()) {
            kanji.onyomi[0]
        } else {
            kanji.kunyomi[0]
        }
    }

    override fun getAnswer(answer: String): String {
        return UtilsLearning.convertStringHiragana(
            answerKana,
            hiraganas
        )
    }

    var kanjis: Array<KanjiInfo>
    lateinit var currentKanji: KanjiInfo
    // We keep track of the answer here because when answering in this quizz, the answer is automatically converted from romaji to hiragana
    // But when we find a good answer in katakana, we fill this with it so the conversion can't be made and the answer stay in katakana
    lateinit var answerKana: String

    var hiraganas: Map<String, String>
    var katakanas: Map<String, String>
}