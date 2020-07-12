package com.xwilarg.yousei

data class SentenceInfo (
    val words: Array<SentenceWordInfo>,
    val meaning: String,
    val particleCount: Int
)