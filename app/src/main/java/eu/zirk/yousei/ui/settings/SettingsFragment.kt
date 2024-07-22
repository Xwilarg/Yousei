package eu.zirk.yousei.ui.settings

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import eu.zirk.yousei.R

class SettingsFragment : Fragment() {
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            findPreference<Preference>("ocr")!!.setOnPreferenceClickListener {
                var modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("ja")
                var model: DigitalInkRecognitionModel =
                    DigitalInkRecognitionModel.builder(modelIdentifier!!).build()
                val remoteModelManager = RemoteModelManager.getInstance()
                val builder = AlertDialog.Builder(activity)
                builder.setMessage("Deleting OCR cache...")
                val p = builder.create()
                p.setCanceledOnTouchOutside(false)
                p.show()
                remoteModelManager.deleteDownloadedModel(model).addOnSuccessListener {
                    p.dismiss()
                    builder.setMessage("OCR cache was deleted")
                    builder.setPositiveButton("OK") { _: DialogInterface, _: Int -> }
                    builder.create().show()
                }.addOnFailureListener { e: Exception ->
                    p.dismiss()
                    builder.setMessage("An error occurred while deleting OCR data: " + e.message!!)
                    builder.setPositiveButton("OK") { _: DialogInterface, _: Int -> }
                    builder.create().show()
                }
                true
            }

            findPreference<Preference>("about")!!.setOnPreferenceClickListener {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle(R.string.app_name)
                builder.setMessage("Made by Christian Chaux")
                builder.setPositiveButton("OK") { _: DialogInterface, _: Int -> }
                builder.create().show()
                true
            }

            findPreference<Preference>("code")!!.setOnPreferenceClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Xwilarg/Yousei")))
                true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_settings, container, false)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()

        return v
    }
}