package eu.zirk.yousei.learning

import com.google.gson.Gson
import eu.zirk.yousei.quizz.IsCorrect
import kotlin.random.Random

class LearningComplete : ILearning {

    constructor(content: String) {
        sentences = Gson().fromJson(content, Array<SentenceInfo>::class.java)
    }

    override fun getQuestion() : Pair<String, String> {
        currentSentence = sentences[Random.nextInt(0, sentences.size)]
        val tmp = currentSentence.words.map { it.word }.toMutableList()
        tmp.shuffle()
        fullSentence = tmp.joinToString(" ")
        return Pair(currentSentence.meaning, "")
    }

    override fun checkAnswer(myAnswer: String) : Pair<IsCorrect, String> {
        return Pair(if (fullSentence == myAnswer) {
            IsCorrect.YES
        } else {
            IsCorrect.NO
        }, fullSentence)
    }

    override fun getCurrent(): String {
        return fullSentence
    }

    override fun displayCurrentInAnswer(): Boolean {
        return false
    }

    override fun getRandomChoices(): ArrayList<String> {
        throw Exception("getRandomChoices should not be called for LearningComplete")
    }

    override fun getAnswer(answer: String): String {
        return answer
    }

    var sentences: Array<SentenceInfo>
    lateinit var fullSentence: String
    lateinit var currentSentence: SentenceInfo
}