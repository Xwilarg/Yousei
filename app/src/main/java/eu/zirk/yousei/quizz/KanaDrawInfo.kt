package eu.zirk.yousei.quizz

data class KanaDrawInfo (
    val width: Int,
    val height: Int,
    val pixels: IntArray,
    var kana: String
)