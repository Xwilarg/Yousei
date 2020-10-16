package com.xwilarg.yousei.learning

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xwilarg.yousei.quizz.IsCorrect
import kotlin.random.Random

class LearningSentence : ILearning {

    constructor(content: String, contentParticles: String, contentHiraganas: String) {
        sentences = Gson().fromJson(content, Array<SentenceInfo>::class.java)
        particles = Gson().fromJson(contentParticles, Array<String>::class.java)
        hiraganas = Gson().fromJson(contentHiraganas, object : TypeToken<Map<String, String>>() {}.type)
        hiraganas = hiraganas.entries.associateBy({ it.value }) {it.key}
    }

    override fun getQuestion() : Pair<String, String> {
        currentSentence = sentences[Random.nextInt(0, sentences.size)]
        fullSentence = prepareSentence()
        return Pair(fullSentence, currentSentence.meaning)
    }

    fun prepareSentence() : String { // TODO: Can be move in a static function since it's also used in LearningComplete
        fullSentence = ""
        var rParticle = Random.nextInt(0, currentSentence.particleCount) // rParticle contains which particle must be guessed
        for (word in currentSentence.words) {
            if (word.isParticle) {
                if (rParticle == 0) {
                    rParticle--
                    fullSentence += "??? "
                    particleAnswer = word.word
                    continue // We continue here so the word is not added to the sentence
                }
                rParticle--
            }
            fullSentence += word.word + " "
        }
        return fullSentence.substring(0, fullSentence.length - 1) // We remove the last space
    }

    override fun checkAnswer(myAnswer: String) : Pair<IsCorrect, String> {
        return Pair(if (UtilsLearning.convertStringHiragana(
                myAnswer,
                hiraganas
            ) == particleAnswer) {
            IsCorrect.YES
        } else {
            IsCorrect.NO
        }, particleAnswer)
    }

    override fun getCurrent(): String {
        return "" // getCurrent is used to display the question in the answer but in this mode, the question is too big to fit
    }

    override fun displayCurrentInAnswer(): Boolean {
        return false
    }

    override fun getRandomChoices(): ArrayList<String> {
        var choices = arrayListOf<String>()
        choices.add(particleAnswer)
        while (choices.size < 4) {
            var randomChoice = particles[Random.nextInt(0, particles.size)]
            if (!choices.contains(randomChoice)) {
                choices.add(randomChoice)
            }
        }
        choices.shuffle()
        return choices
    }

    override fun getAnswer(answer: String): String {
        return UtilsLearning.convertStringHiragana(
            answer,
            hiraganas
        )
    }

    var sentences: Array<SentenceInfo>
    var particles: Array<String>
    lateinit var fullSentence: String
    lateinit var particleAnswer: String
    lateinit var currentSentence: SentenceInfo

    var hiraganas: Map<String, String>
}