package com.easylearn.easyverbs.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {

    companion object {
        private val QUESTION_COUNT = intPreferencesKey("question_count")
        private val FORMS_V1 = booleanPreferencesKey("forms_v1")
        private val FORMS_V2 = booleanPreferencesKey("forms_v2")
        private val FORMS_V3 = booleanPreferencesKey("forms_v3")
        private val DARK_MODE = booleanPreferencesKey("dark_mode")
        private val COMPLEXITY = stringPreferencesKey("complexity")
        private val GRADIENT_FROM = stringPreferencesKey("gradient_from")
        private val GRADIENT_TO = stringPreferencesKey("gradient_to")
        private val SHOW_TRANSLATION = booleanPreferencesKey("show_translation")
        private val AUTO_ADVANCE = booleanPreferencesKey("auto_advance")
        private val SHUFFLE = booleanPreferencesKey("shuffle")
        private val COMPACT = booleanPreferencesKey("compact")
        private val LANG = stringPreferencesKey("lang")
        private val FAV_ONLY = booleanPreferencesKey("fav_only")
        private val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        private val SPACED_REP = booleanPreferencesKey("spaced_rep")
        private val SMART_ORDER = booleanPreferencesKey("smart_order")
        private val HAPTICS = booleanPreferencesKey("haptics")
        private val VERB_GROUP = stringPreferencesKey("verb_group")
        private val ACCENT_PRESET = stringPreferencesKey("accent_preset")
        private val TOTAL_CORRECT = intPreferencesKey("total_correct")
        private val TOTAL_QUESTIONS = intPreferencesKey("total_questions")
        private val BEST_STREAK = intPreferencesKey("best_streak")
        private val SESSIONS_COMPLETED = intPreferencesKey("sessions_completed")
        private val SPEED_HIGH_SCORE = intPreferencesKey("speed_high_score")
        private val VERBS_LEARNED = stringPreferencesKey("verbs_learned")
        private val ACTIVITY_LOG = stringPreferencesKey("activity_log")
    }

    val questionCount: Flow<Int> = context.dataStore.data.map { it[QUESTION_COUNT] ?: 10 }
    val formsV1: Flow<Boolean> = context.dataStore.data.map { it[FORMS_V1] ?: true }
    val formsV2: Flow<Boolean> = context.dataStore.data.map { it[FORMS_V2] ?: true }
    val formsV3: Flow<Boolean> = context.dataStore.data.map { it[FORMS_V3] ?: true }
    val darkMode: Flow<Boolean> = context.dataStore.data.map { it[DARK_MODE] ?: false }
    val complexity: Flow<String> = context.dataStore.data.map { it[COMPLEXITY] ?: "all" }
    val gradientFrom: Flow<String> = context.dataStore.data.map { it[GRADIENT_FROM] ?: "#4f46e5" }
    val gradientTo: Flow<String> = context.dataStore.data.map { it[GRADIENT_TO] ?: "#7c3aed" }
    val showTranslation: Flow<Boolean> = context.dataStore.data.map { it[SHOW_TRANSLATION] ?: true }
    val autoAdvance: Flow<Boolean> = context.dataStore.data.map { it[AUTO_ADVANCE] ?: false }
    val shuffle: Flow<Boolean> = context.dataStore.data.map { it[SHUFFLE] ?: true }
    val compact: Flow<Boolean> = context.dataStore.data.map { it[COMPACT] ?: false }
    val lang: Flow<String> = context.dataStore.data.map { it[LANG] ?: "ru" }
    val favOnly: Flow<Boolean> = context.dataStore.data.map { it[FAV_ONLY] ?: false }
    val soundEnabled: Flow<Boolean> = context.dataStore.data.map { it[SOUND_ENABLED] ?: true }
    val spacedRep: Flow<Boolean> = context.dataStore.data.map { it[SPACED_REP] ?: false }
    val smartOrder: Flow<Boolean> = context.dataStore.data.map { it[SMART_ORDER] ?: true }
    val haptics: Flow<Boolean> = context.dataStore.data.map { it[HAPTICS] ?: true }
    val verbGroup: Flow<String> = context.dataStore.data.map { it[VERB_GROUP] ?: "all" }
    val accentPreset: Flow<String> = context.dataStore.data.map { it[ACCENT_PRESET] ?: "indigo" }
    val totalCorrect: Flow<Int> = context.dataStore.data.map { it[TOTAL_CORRECT] ?: 0 }
    val totalQuestions: Flow<Int> = context.dataStore.data.map { it[TOTAL_QUESTIONS] ?: 0 }
    val bestStreak: Flow<Int> = context.dataStore.data.map { it[BEST_STREAK] ?: 0 }
    val sessionsCompleted: Flow<Int> = context.dataStore.data.map { it[SESSIONS_COMPLETED] ?: 0 }
    val speedHighScore: Flow<Int> = context.dataStore.data.map { it[SPEED_HIGH_SCORE] ?: 0 }

    suspend fun setQuestionCount(value: Int) = context.dataStore.edit { it[QUESTION_COUNT] = value }
    suspend fun setFormsV1(value: Boolean) = context.dataStore.edit { it[FORMS_V1] = value }
    suspend fun setFormsV2(value: Boolean) = context.dataStore.edit { it[FORMS_V2] = value }
    suspend fun setFormsV3(value: Boolean) = context.dataStore.edit { it[FORMS_V3] = value }
    suspend fun setDarkMode(value: Boolean) = context.dataStore.edit { it[DARK_MODE] = value }
    suspend fun setComplexity(value: String) = context.dataStore.edit { it[COMPLEXITY] = value }
    suspend fun setGradientFrom(value: String) = context.dataStore.edit { it[GRADIENT_FROM] = value }
    suspend fun setGradientTo(value: String) = context.dataStore.edit { it[GRADIENT_TO] = value }
    suspend fun setShowTranslation(value: Boolean) = context.dataStore.edit { it[SHOW_TRANSLATION] = value }
    suspend fun setAutoAdvance(value: Boolean) = context.dataStore.edit { it[AUTO_ADVANCE] = value }
    suspend fun setShuffle(value: Boolean) = context.dataStore.edit { it[SHUFFLE] = value }
    suspend fun setCompact(value: Boolean) = context.dataStore.edit { it[COMPACT] = value }
    suspend fun setLang(value: String) = context.dataStore.edit { it[LANG] = value }
    suspend fun setFavOnly(value: Boolean) = context.dataStore.edit { it[FAV_ONLY] = value }
    suspend fun setSoundEnabled(value: Boolean) = context.dataStore.edit { it[SOUND_ENABLED] = value }
    suspend fun setSpacedRep(value: Boolean) = context.dataStore.edit { it[SPACED_REP] = value }
    suspend fun setSmartOrder(value: Boolean) = context.dataStore.edit { it[SMART_ORDER] = value }
    suspend fun setHaptics(value: Boolean) = context.dataStore.edit { it[HAPTICS] = value }
    suspend fun setVerbGroup(value: String) = context.dataStore.edit { it[VERB_GROUP] = value }
    suspend fun setAccentPreset(value: String) = context.dataStore.edit { it[ACCENT_PRESET] = value }

    suspend fun updateStats(correct: Int, total: Int, streak: Int) = context.dataStore.edit {
        it[TOTAL_CORRECT] = (it[TOTAL_CORRECT] ?: 0) + correct
        it[TOTAL_QUESTIONS] = (it[TOTAL_QUESTIONS] ?: 0) + total
        if (streak > (it[BEST_STREAK] ?: 0)) it[BEST_STREAK] = streak
        it[SESSIONS_COMPLETED] = (it[SESSIONS_COMPLETED] ?: 0) + 1
    }

    suspend fun updateSpeedHighScore(score: Int) = context.dataStore.edit {
        if (score > (it[SPEED_HIGH_SCORE] ?: 0)) it[SPEED_HIGH_SCORE] = score
    }

    suspend fun resetAll() = context.dataStore.edit { it.clear() }
}
