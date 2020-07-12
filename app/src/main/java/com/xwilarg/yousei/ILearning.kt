package com.xwilarg.yousei

interface ILearning {

    /// Return the string to display as a question, return value is question and the help under it
    fun getQuestion() : Pair<String, String>

    /// Check is the answer is right, return value is if the answer is correct and the closest answer found
    fun checkAnswer(myAnswer: String) : Pair<IsCorrect, String>

    fun getCurrent() : String

    fun getRandomChoices() : ArrayList<String>

    fun getAnswer(answer: String) : String
}