package com.example.audio

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class SoundManager(context: Context) : TextToSpeech.OnInitListener {
    private var toneGenerator: ToneGenerator? = null
    private var tts: TextToSpeech? = null
    private var isTtsReady: Boolean = false
    var isSoundEnabled: Boolean = true

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
        } catch (e: Exception) {
            Log.e("SoundManager", "Failed to initialize ToneGenerator", e)
        }
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("SoundManager", "Failed to initialize TextToSpeech", e)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            try {
                val urduLocale = Locale("ur", "PK")
                val result = tts?.setLanguage(urduLocale)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.setLanguage(Locale.US)
                }
                isTtsReady = true
            } catch (e: Exception) {
                Log.e("SoundManager", "Error setting TTS language", e)
            }
        }
    }

    fun setLanguage(appLanguage: com.example.data.AppLanguage) {
        if (!isTtsReady || tts == null) return
        try {
            when (appLanguage) {
                com.example.data.AppLanguage.ENGLISH -> tts?.setLanguage(Locale.US)
                com.example.data.AppLanguage.URDU -> {
                    val urduLocale = Locale("ur", "PK")
                    val result = tts?.setLanguage(urduLocale)
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        tts?.setLanguage(Locale.US)
                    }
                }
                com.example.data.AppLanguage.ROMAN -> tts?.setLanguage(Locale.US)
            }
        } catch (e: Exception) {
            Log.e("SoundManager", "Error updating TTS language", e)
        }
    }

    fun speakText(rawText: String) {
        if (!isSoundEnabled) return
        try {
            val cleanText = rawText.replace(Regex("[\\uD800-\\uDBFF][\\uDC00-\\uDFFF]|\\p{So}|\\p{Cn}"), "").trim()
            if (cleanText.isNotBlank() && isTtsReady && tts != null) {
                tts?.stop()
                tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "QuestionSpeech")
            }
        } catch (e: Exception) {
            Log.e("SoundManager", "Error speaking text via TTS", e)
        }
    }

    fun stopSpeaking() {
        try {
            if (tts?.isSpeaking == true) {
                tts?.stop()
            }
        } catch (e: Exception) {
            Log.e("SoundManager", "Error stopping TTS", e)
        }
    }

    fun playCorrectSound() {
        if (!isSoundEnabled) return
        stopSpeaking()
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
        } catch (e: Exception) {
            Log.e("SoundManager", "Error playing correct sound", e)
        }
    }

    fun playWrongSound() {
        if (!isSoundEnabled) return
        stopSpeaking()
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_LOW_PBX_L, 350)
        } catch (e: Exception) {
            Log.e("SoundManager", "Error playing wrong sound", e)
        }
    }

    fun playCompletionSound() {
        if (!isSoundEnabled) return
        stopSpeaking()
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 400)
        } catch (e: Exception) {
            Log.e("SoundManager", "Error playing completion sound", e)
        }
    }

    fun release() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
            Log.e("SoundManager", "Error releasing TTS", e)
        }
        toneGenerator?.release()
        toneGenerator = null
    }
}
