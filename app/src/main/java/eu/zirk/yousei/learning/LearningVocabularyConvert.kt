package eu.zirk.yousei.learning;

import com.google.gson.Gson
import eu.zirk.yousei.quizz.IsCorrect
import kotlin.random.Random

class LearningVocabularyConvert : ILearning {

    constructor(content: String) {
        words = Gson().fromJson(content, Array<VocabularyInfo>::class.java)
    }

    override fun getQuestion() : Pair<String, String> {
        currentWord = words[Random.nextInt(0, words.size)]
        return Pair(currentWord.reading, currentWord.meaning[0])
    }

    override fun checkAnswer(myAnswer: String) : Pair<IsCorrect, String> {
        return if (myAnswer == currentWord.word) {
            return Pair(IsCorrect.YES, currentWord.word)
        } else if (currentWord.word.contains(myAnswer) && !myAnswer.isNullOrBlank()) {
            return Pair(IsCorrect.PARTIAL, currentWord.word)
        } else {
            return Pair(IsCorrect.NO, currentWord.word)
        }
    }

    override fun getCurrent(): String {
        return currentWord.reading
    }

    override fun displayCurrentInAnswer(): Boolean {
        return true
    }

    override fun getRandomChoices(): ArrayList<String> {
        var choices = arrayListOf<String>()
        choices.add(currentWord.word)
        while (choices.size < 4) {
            var randomChoice = words[Random.nextInt(0, words.size)].word
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

    var words: Array<VocabularyInfo>
    lateinit var currentWord: VocabularyInfo
}
