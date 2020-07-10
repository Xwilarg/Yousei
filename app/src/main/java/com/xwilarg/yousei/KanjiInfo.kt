package com.xwilarg.yousei

data class KanjiInfo (
    val kanji: String,
    val meaning: Array<String>,
    val onyomi: Array<String>,
    val kunyomi: Array<String>,
    val imageId: String
)