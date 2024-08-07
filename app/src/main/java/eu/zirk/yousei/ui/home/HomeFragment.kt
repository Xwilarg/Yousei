package eu.zirk.yousei.ui.home

import android.content.Context.MODE_PRIVATE
import eu.zirk.yousei.R
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import eu.zirk.yousei.learning.LearningType
import eu.zirk.yousei.quizz.QuizzChoicesActivity
import eu.zirk.yousei.quizz.QuizzCompleteActivity
import eu.zirk.yousei.quizz.QuizzDrawActivity
import eu.zirk.yousei.quizz.QuizzNormalActivity


class HomeFragment : Fragment() {
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

        val preferences = requireActivity().getPreferences(MODE_PRIVATE)
        if (preferences.contains("defaultMode")) {
            v.findViewById<RadioGroup>(R.id.radioGroup).check(preferences.getInt("defaultMode", 0))

            // TODO: Dupplicate with onRadioGroupClick
            val isDraw =  v.findViewById<RadioButton>(R.id.radioDraw).isChecked
            v.findViewById<Button>(R.id.buttonQuizzHiragana).isEnabled = !isDraw
            v.findViewById<Button>(R.id.buttonQuizzKatakana).isEnabled = !isDraw
            v.findViewById<Button>(R.id.buttonQuizzKanji).isEnabled = !isDraw
            v.findViewById<Button>(R.id.buttonQuizzVocabulary).isEnabled = !isDraw
            val isInput =  v.findViewById<RadioButton>(R.id.radioFreeText).isChecked
            v.findViewById<Button>(R.id.buttonQuizzKanjiConvert).isEnabled = !isInput
            v.findViewById<Button>(R.id.buttonQuizzVocabularyConvert).isEnabled = !isInput
        }
        if (preferences.contains("defaultJlpt")) {
            v.findViewById<EditText>(R.id.jlptValue).setText(preferences.getString("defaultJlpt", "6"))
        }

        v.findViewById<Button>(R.id.radioChoices).setOnClickListener {
            onRadioGroupClick(it)
        }

        v.findViewById<Button>(R.id.radioFreeText).setOnClickListener {
            onRadioGroupClick(it)
        }

        v.findViewById<Button>(R.id.radioDraw).setOnClickListener {
            onRadioGroupClick(it)
        }

        v.findViewById<Button>(R.id.buttonQuizzHiragana).setOnClickListener {
            startGameHiragana(it)
        }

        v.findViewById<Button>(R.id.buttonQuizzKatakana).setOnClickListener {
            startGameKatakana(it)
        }

        v.findViewById<Button>(R.id.buttonQuizzKanji).setOnClickListener {
            startGameKanji(it)
        }

        v.findViewById<Button>(R.id.buttonQuizzKanjiConvert).setOnClickListener {
            startGameKanjiConvert(it)
        }

        v.findViewById<Button>(R.id.buttonQuizzKanjiReading).setOnClickListener {
            startGameKanjiReading(it)
        }

        v.findViewById<Button>(R.id.buttonQuizzVocabulary).setOnClickListener {
            startGameVocabulary(it)
        }

        v.findViewById<Button>(R.id.buttonQuizzVocabularyConvert).setOnClickListener {
            startGameVocabularyConvert(it)
        }

        v.findViewById<Button>(R.id.buttonQuizzVocabularyReading).setOnClickListener {
            startGameVocabularyReading(it)
        }

        v.findViewById<Button>(R.id.buttonQuizzParticles).setOnClickListener {
            startGameSentence(it)
        }

        v.findViewById<Button>(R.id.buttonQuizzComplete).setOnClickListener {
            startGameComplete(it)
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

    fun startGameComplete(view: View) {
        val intent = Intent(activity, QuizzCompleteActivity::class.java)
        intent.putExtra("LEARNING_TYPE", LearningType.COMPLETE)
        intent.putExtra("JLPT", jlptValue)
        startActivity(intent)
    }

    private fun getQuizzType() : Class<*> {

        // This function is called when we go in a quizz
        // We use that time to save the last quizz user preferences
        val preferences = requireActivity().getPreferences(MODE_PRIVATE)
        with (preferences.edit()) {
            putInt("defaultMode", (requireActivity().findViewById<RadioGroup>(R.id.radioGroup)).checkedRadioButtonId)
            putString("defaultJlpt", (requireActivity().findViewById<EditText>(R.id.jlptValue)).text.toString())
            apply()
        }


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