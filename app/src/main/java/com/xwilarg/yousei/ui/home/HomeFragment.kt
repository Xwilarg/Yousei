package com.xwilarg.yousei.ui.home

import com.xwilarg.yousei.R
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.xwilarg.yousei.learning.LearningType
import com.xwilarg.yousei.quizz.QuizzChoicesActivity
import com.xwilarg.yousei.quizz.QuizzDrawActivity
import com.xwilarg.yousei.quizz.QuizzNormalActivity
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(com.xwilarg.yousei.R.layout.fragment_home)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_home, container, false)
        v.findViewById<EditText>(R.id.jlptValue).addTextChangedListener { text: Editable? -> run {
            if (text != null && text.isNotEmpty()) {
                val value = text.toString().toInt()
                if (value < 1 || value > 5)
                    v.findViewById<EditText>(R.id.jlptValue).setText(jlptValue.toString())
                else
                    jlptValue = value
            }
        }}

        (v.radioChoices as Button).setOnClickListener {
            onRadioGroupClick(it)
        }

        (v.radioFreeText as Button).setOnClickListener {
            onRadioGroupClick(it)
        }

        (v.radioDraw as Button).setOnClickListener {
            onRadioGroupClick(it)
        }

        (v.buttonQuizzHiragana as Button).setOnClickListener {
            startGameHiragana(it)
        }

        (v.buttonQuizzKatakana as Button).setOnClickListener {
            startGameKatakana(it)
        }

        (v.buttonQuizzKanji as Button).setOnClickListener {
            startGameKanji(it)
        }

        (v.buttonQuizzKanjiConvert as Button).setOnClickListener {
            startGameKanjiConvert(it)
        }

        (v.buttonQuizzKanjiReading as Button).setOnClickListener {
            startGameKanjiReading(it)
        }

        (v.buttonQuizzVocabulary as Button).setOnClickListener {
            startGameVocabulary(it)
        }

        (v.buttonQuizzVocabularyConvert as Button).setOnClickListener {
            startGameVocabularyConvert(it)
        }

        (v.buttonQuizzVocabularyReading as Button).setOnClickListener {
            startGameVocabularyReading(it)
        }

        (v.buttonQuizzParticles as Button).setOnClickListener {
            startGameSentence(it)
        }

        return v
    }

    fun onRadioGroupClick(view: View) {
        val isDraw =  requireActivity().findViewById<RadioButton>(R.id.radioDraw).isChecked
        requireActivity().findViewById<Button>(R.id.buttonQuizzHiragana).isEnabled = !isDraw
        requireActivity().findViewById<Button>(R.id.buttonQuizzKatakana).isEnabled = !isDraw
        requireActivity().findViewById<Button>(R.id.buttonQuizzKanji).isEnabled = !isDraw
        requireActivity().findViewById<Button>(R.id.buttonQuizzVocabulary).isEnabled = !isDraw
        val isInput =  requireActivity().findViewById<RadioButton>(R.id.radioFreeText).isChecked
        requireActivity().findViewById<Button>(R.id.buttonQuizzKanjiConvert).isEnabled = !isInput
        requireActivity().findViewById<Button>(R.id.buttonQuizzVocabularyConvert).isEnabled = !isInput
    }
    
    fun startGameHiragana(view: View) {
        val intent = Intent(activity, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.HIRAGANA)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameKatakana(view: View) {
        val intent = Intent(activity, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.KATAKANA)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameKanji(view: View) {
        val intent = Intent(activity, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.KANJI_TRANSLATE)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameKanjiReading(view: View) {
        val intent = Intent(activity, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.KANJI_READ)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameKanjiConvert(view: View) {
        val intent = Intent(activity, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.KANJI_CONVERT)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameVocabulary(view: View) {
        val intent = Intent(activity, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.VOCABULARY_TRANSLATE)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameVocabularyReading(view: View) {
        val intent = Intent(activity, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.VOCABULARY_READ)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameVocabularyConvert(view: View) {
        val intent = Intent(activity, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.VOCABULARY_CONVERT)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    fun startGameSentence(view: View) {
        val intent = Intent(activity, getQuizzType())
        intent.putExtra("LEARNING_TYPE", LearningType.SENTENCE)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    private fun getQuizzType() : Class<*> {
        return when {
            requireActivity().findViewById<RadioButton>(R.id.radioChoices).isChecked -> {
                QuizzChoicesActivity::class.java
            }
            requireActivity().findViewById<RadioButton>(R.id.radioFreeText).isChecked -> {
                QuizzNormalActivity::class.java
            }
            else -> {
                QuizzDrawActivity::class.java
            }
        }
    }

    private var jlptValue = 5
}