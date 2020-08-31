package com.xwilarg.yousei.learning

import com.google.gson.Gson
import com.xwilarg.yousei.quizz.IsCorrect
import kotlin.random.Random

class LearningKanjiConvert : ILearning {

    constructor(content: String) {
        kanjis = Gson().fromJson(content, Array<KanjiInfo>::class.java)
    }

    override fun getQuestion() : Pair<String, String> {
        currentKanji = kanjis[Random.nextInt(0, kanjis.size)]
        return Pair(, if (currentKanji.kunyomi.isEmpty()) {
            currentKanji.onyomi.get(0)
        } else {
            currentKanji.kunyomi.get(0)
        }, currentKanji.meaning[0])
    }

    override fun checkAnswer(myAnswer: String) : Pair<IsCorrect, String> {
        return if (myAnswer == currentKanji.kanji) {
            return Pair(IsCorrect.NO, currentKanji.kanji)
        } else {
            return Pair(IsCorrect.YES, currentKanji.kanji)
        }
    }

    override fun getCurrent(): String {
        return currentKanji.kanji
    }

    override fun getRandomChoices(): ArrayList<String> {
        var choices = arrayListOf<String>()
        var goodAnswer = currentKanji.kanji
        choices.add(goodAnswer)
        var dotValue = ""
        // If the word is like "い.く" we add ".く" to all the answers so the player can't guess just with that
        if (goodAnswer.contains(".")) {
            dotValue = "." + goodAnswer.split(".")[1]
        }
        while (choices.size < 4) {
            var randomChoice = kanjis[Random.nextInt(0, kanjis.size)].kanji
            if (dotValue != "") {
                if (randomChoice.contains(".") && randomChoice.split(".")[1] == dotValue) { // If the bad answer already contains a . and it's the same ending as the good answer
                    // Do nothing
                } else if (randomChoice.contains(".")) { // The bad answer have a . but not the same ending
                    randomChoice = randomChoice.split(".")[0] + dotValue // We take the base word and add our own ending
                } else {
                    randomChoice += dotValue // We just add our ending
                }
            } else if (randomChoice.contains(".")) { // The good answer doesn't contains a . but the bad answer does
                randomChoice = randomChoice.split(".")[0] // We just remove the ending
            }
            if (!choices.contains(randomChoice)) {
                choices.add(randomChoice)
            }
        }
        choices.shuffle()
        return choices
    }

    override fun getAnswer(answer: String): String {
        return currentKanji.kanji
    }

    var kanjis: Array<KanjiInfo>
    lateinit var currentKanji: KanjiInfo
}