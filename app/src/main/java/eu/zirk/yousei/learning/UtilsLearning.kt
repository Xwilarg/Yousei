package eu.zirk.yousei.learning

object UtilsLearning {
    fun convertStringHiragana(str: String, refDict: Map<String, String>): String {
        return convertString(
            str,
            refDict,
            'っ',
            'ゃ',
            'ゅ',
            'ょ'
        )
    }

    fun convertStringKatakana(str: String, refDict: Map<String, String>): String {
        return convertString(
            str,
            refDict,
            'ッ',
            'ャ',
            'ュ',
            'ョ'
        )
    }

    // Convert a string using a Map
    // str is the string to convert
    // refDict is the reference dictionary used for the conversion
    // double is the character used when we do 2 letters in a row (like rr)
    private fun convertString(str: String, refDict: Map<String, String>, double: Char, ya: Char, yu: Char, yo: Char): String {
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
            // Used for diphtongs
            var thirdLetter = if (currAnswer.length > 2) {
                currAnswer[2]
            } else {
                null
            }
            if (currAnswer.length > 2 && refDict.containsKey(currAnswer.substring(0, 3))) { // chi, shi
                word += refDict[currAnswer.substring(0, 3)]
                currAnswer = currAnswer.substring(3)
            }
            // Diphthongs, like in ryu
            // if word length is bigger than 2
            // if second letter is y and xi is valid (example: ryu -> ri)
            // or if second letter is h and xhi is valid (example: chotto -> chi)
            // With that, the third letter must be a, u or o
            else if (thirdLetter != null
                    && ((currAnswer[1] == 'y' && refDict.containsKey(currAnswer[0] + "i"))
                    || (currAnswer[1] == 'h' && refDict.containsKey(currAnswer[0] + "hi")))
                && (thirdLetter == 'a' || thirdLetter == 'u' || thirdLetter == 'o')) {
                word += if (currAnswer[1] == 'y') {
                    refDict[currAnswer[0] + "i"]
                } else {
                    refDict[currAnswer[0] + "hi"]
                } + when (thirdLetter) {
                    'a' -> {
                        ya
                    }
                    'u' -> {
                        yu
                    }
                    else -> {
                        yo
                    }
                }
                currAnswer = currAnswer.substring(3)
            } else if (currAnswer[0] == currAnswer[1]) { // Two times the same letter, like in gakkou
                word += double
                currAnswer = currAnswer.substring(1)
            } else if (refDict.containsKey(currAnswer.substring(0, 2))) {
                word += refDict[currAnswer.substring(0, 2)]
                currAnswer = currAnswer.substring(2)
            } else if (refDict.containsKey(currAnswer.substring(0, 1))) { // n, a, i, u, e, o
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