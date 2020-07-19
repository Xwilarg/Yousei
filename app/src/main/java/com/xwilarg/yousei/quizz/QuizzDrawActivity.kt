package com.xwilarg.yousei.quizz

import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xwilarg.yousei.DrawingView
import com.xwilarg.yousei.R
import kotlin.math.floor

class QuizzDrawActivity : QuizzCommon() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hiraganaDrawInfo = Gson().fromJson(this.resources.openRawResource(R.raw.hiragana_draw).bufferedReader().use { it.readText() }, object : TypeToken<Array<KanaDrawInfo>>() {}.type)
        katakanaDrawInfo = Gson().fromJson(this.resources.openRawResource(R.raw.katakana_draw).bufferedReader().use { it.readText() }, object : TypeToken<Array<KanaDrawInfo>>() {}.type)
        katakanaDrawInfo = Gson().fromJson(this.resources.openRawResource(R.raw.katakana_draw).bufferedReader().use { it.readText() }, object : TypeToken<Array<KanaDrawInfo>>() {}.type)
        setContentView(R.layout.activity_quizz_draw)
        findViewById<View>(R.id.viewDraw)
        preload()
    }

    fun answer(view: View) {
        val btm = findViewById<DrawingView>(R.id.viewDraw).getContent()
        val pixels = IntArray(btm.width * btm.height)
        btm.getPixels(pixels, 0, btm.width, 0, 0, btm.width, btm.height)
        val tmp = ArrayList<ArrayList<Int>>()
        var i = 0;
        // We put all pixels in a 2D array
        var currTmp = ArrayList<Int>()
        for (p in pixels){
            currTmp.add(if (p == -3487030) {
                0
            } else {
                1
            })
            i++
            if (i == btm.width) {
                i = 0
                tmp.add(currTmp)
                currTmp = ArrayList()
            }
        }

        // Trim the 4 borders
        while (tmp.all {it[0] == 0})
            for (i in 0 until tmp.size) tmp[i].removeAt(0)

        while (tmp.all {it[it.size - 1] == 0})
            for (i in 0 until tmp.size) tmp[i].removeAt(tmp[i].size - 1)

        while (tmp[0].all {it == 0})
            tmp.removeAt(0)

        while (tmp[tmp.size - 1].all {it == 0})
            tmp.removeAt(tmp.size - 1)

        var userArray = Array(tmp.size) { Array(tmp[0].size, init= { 0f }) }
        for (y in userArray.indices) {
            for (x in userArray[y].indices) {
                userArray[y][x] = tmp[y][x].toFloat()
            }
        }
        userArray = downSizeBy2(downSizeBy2(downSizeBy2(userArray)))
        val closestHiragana = getClosest(userArray, hiraganaDrawInfo)
        checkAnswer(closestHiragana.first)
        findViewById<DrawingView>(R.id.viewDraw).clear()
    }

    // Divide the size of a 2D array of pixel by 2
    fun downSizeBy2(pixels: Array<Array<Float>>) : Array<Array<Float>> {
        val finalPixels = Array(pixels.size / 2) { Array(pixels[0].size / 2, init= { 0f }) }
        for (y in finalPixels.indices) {
            for (x in finalPixels[y].indices) {
                finalPixels[y][x] = (pixels[y * 2][x * 2] + pixels[y * 2 + 1][x * 2] + pixels[y * 2][x * 2 + 1] + pixels[y * 2 + 1][x * 2 + 1]) / 4;
            }
        }
        return finalPixels
    }

    fun getClosest(pixels: Array<Array<Float>>, info: Array<KanaDrawInfo>): Pair<String, Int> {
        var bestKana = ""
        var bestScore = -1

        for (kana in info) {
            val width = kana.width
            val height = kana.height

            val splitWidth = width / pixels[0].size.toFloat()
            val splitHeigth = height / pixels.size.toFloat()

            var size = ((pixels[0].size.toFloat() / width) * (pixels.size.toFloat() / height)) / 8

            // Create a new pixel array that have the same size of the kana we are comparing
            var myPixels = Array(height) { Array(width, init= { 0 }) }
            for (y in pixels.indices) {
                for (x in pixels[0].indices) {
                    myPixels[floor(y * splitHeigth).toInt()][floor(x * splitWidth).toInt()] += pixels[y][x].toInt()
                }
            }

            // Compare what we drew to the kana and give a score depending on that
            var score = 0
            var c = 0
            for (y in myPixels.indices) {
                for (x in myPixels[0].indices) {
                    if ((myPixels[y][x] > size && kana.pixels[c] == 1)
                        || (myPixels[y][x] <= size && kana.pixels[c] == 0))
                        score++
                    c++
                }
            }

            println(kana.kana + ": " + score)
            if (score > bestScore)
            {
                bestScore = score
                bestKana = kana.kana
            }
        }
        return Pair(bestKana, bestScore)
    }

    fun clear(view: View) {
        findViewById<DrawingView>(R.id.viewDraw).clear()
    }

    lateinit var hiraganaDrawInfo: Array<KanaDrawInfo>
    lateinit var katakanaDrawInfo: Array<KanaDrawInfo>
}