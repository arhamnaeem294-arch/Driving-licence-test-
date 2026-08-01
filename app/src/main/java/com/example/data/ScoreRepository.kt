package com.example.data

import android.content.Context
import android.content.SharedPreferences

class ScoreRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("pak_driving_test_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_HIGH_SCORE = "key_high_score"
        private const val KEY_GAMES_PLAYED = "key_games_played"
        private const val KEY_MAX_CORRECT = "key_max_correct"
        private const val KEY_SOUND_ENABLED = "key_sound_enabled"
        private const val KEY_APP_LANGUAGE = "key_app_language"
        private const val KEY_ADMOB_BANNER_ID = "key_admob_banner_id"
        const val DEFAULT_ADMOB_TEST_BANNER_ID = "ca-app-pub-3940256099942544/6300978111"
    }

    fun getAdMobBannerId(): String {
        return prefs.getString(KEY_ADMOB_BANNER_ID, DEFAULT_ADMOB_TEST_BANNER_ID) ?: DEFAULT_ADMOB_TEST_BANNER_ID
    }

    fun setAdMobBannerId(bannerId: String) {
        val cleanId = bannerId.trim()
        val finalId = if (cleanId.isNotBlank()) cleanId else DEFAULT_ADMOB_TEST_BANNER_ID
        prefs.edit().putString(KEY_ADMOB_BANNER_ID, finalId).apply()
    }

    fun getAppLanguage(): String {
        return prefs.getString(KEY_APP_LANGUAGE, "ur") ?: "ur"
    }

    fun setAppLanguage(langCode: String) {
        prefs.edit().putString(KEY_APP_LANGUAGE, langCode).apply()
    }

    fun getHighScore(): Int {
        return prefs.getInt(KEY_HIGH_SCORE, 0)
    }

    fun saveHighScoreIfHigher(score: Int): Boolean {
        val currentHigh = getHighScore()
        if (score > currentHigh) {
            prefs.edit().putInt(KEY_HIGH_SCORE, score).apply()
            return true
        }
        return false
    }

    fun getMaxCorrectAnswers(): Int {
        return prefs.getInt(KEY_MAX_CORRECT, 0)
    }

    fun saveMaxCorrectIfHigher(correctCount: Int) {
        val currentMax = getMaxCorrectAnswers()
        if (correctCount > currentMax) {
            prefs.edit().putInt(KEY_MAX_CORRECT, correctCount).apply()
        }
    }

    fun getGamesPlayed(): Int {
        return prefs.getInt(KEY_GAMES_PLAYED, 0)
    }

    fun incrementGamesPlayed() {
        val current = getGamesPlayed()
        prefs.edit().putInt(KEY_GAMES_PLAYED, current + 1).apply()
    }

    fun isSoundEnabled(): Boolean {
        return prefs.getBoolean(KEY_SOUND_ENABLED, true)
    }

    fun setSoundEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply()
    }
}
