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
        return Pair(if (currentKanji.kunyomi.isEmpty()) {
            currentKanji.onyomi[0]
        } else {
            currentKanji.kunyomi[0]
        }, currentKanji.meaning[0])
    }

    override fun checkAnswer(myAnswer: String) : Pair<IsCorrect, String> {
        if (myAnswer == currentKanji.kanji) {
            return Pair(IsCorrect.YES, currentKanji.kanji)
        }
        var m = myAnswer;
        if (m.contains(".")) {
            if (currentKanji.kanji == m.replace(".", "")) {
                return Pair(IsCorrect.YES, m)
            }
            m = m.split(".")[0]
        }
        if (m == currentKanji.kanji[0].toString()) {
            return Pair(IsCorrect.YES, currentKanji.kanji[0].toString())
        }
        return Pair(IsCorrect.NO, currentKanji.kanji)
    }

    override fun getCurrent(): String {
        return if (currentKanji.kunyomi.isEmpty()) {
            currentKanji.onyomi[0]
        } else {
            currentKanji.kunyomi[0]
        }
    }

    override fun getRandomChoices(): ArrayList<String> {
        var choices = arrayListOf<String>()
        var goodAnswer = currentKanji.kanji
        choices.add(goodAnswer)
        while (choices.size < 4) {
            var randomChoice = kanjis[Random.nextInt(0, kanjis.size)].kanji
            if (randomChoice.contains(".")) { // The good answer doesn't contains a . but the bad answer does
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
        return answer
    }

    var kanjis: Array<KanjiInfo>
    lateinit var currentKanji: KanjiInfo
}