package com.xwilarg.yousei

object UtilsLearning {
    fun convertString(str: String, refDict: Map<String, String>): String {
        var word = ""
        var currAnswer = str
        while (currAnswer.isNotEmpty()) {
            if (currAnswer.length == 1) {
                if (refDict.containsKey(currAnswer.substring(0, 1))) {
                    word += refDict[currAnswer]
                } else {
                    word += currAnswer
                }
                break
            }
            if (refDict.containsKey(currAnswer.substring(0, 2))) {
                word += refDict[currAnswer.substring(0, 2)]
                currAnswer = currAnswer.substring(2)
            } else if (refDict.containsKey(currAnswer.substring(0, 1))) {
                word += refDict[currAnswer.substring(0, 1)]
                currAnswer = currAnswer.substring(1)
            } else {
                word += currAnswer[0]
                currAnswer = currAnswer.substring(1)
                continue
            }
        }
        return word
    }
}