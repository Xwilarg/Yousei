package com.xwilarg.yousei.learning

data class SentenceInfo (
    val words: Array<SentenceWordInfo>,
    val meaning: String,
    val particleCount: Int
)