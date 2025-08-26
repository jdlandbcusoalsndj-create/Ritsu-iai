package com.ritsu.ai.assistant.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ritsu_preferences")

class PreferenceManager(private val context: Context) {

    // Claves de preferencias
    companion object {
        // Configuración general
        private val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
        private val THEME_MODE = stringPreferencesKey("theme_mode")
        private val LANGUAGE = stringPreferencesKey("language")

        // Configuración de Ritsu
        private val RITSU_PERSONALITY = stringPreferencesKey("ritsu_personality")
        private val RITSU_VOICE_SPEED = floatPreferencesKey("ritsu_voice_speed")
        private val RITSU_VOICE_PITCH = floatPreferencesKey("ritsu_voice_pitch")
        private val RITSU_VOICE_LANGUAGE = stringPreferencesKey("ritsu_voice_language")

        // Configuración de IA
        private val AI_MODEL_TYPE = stringPreferencesKey("ai_model_type")
        private val AI_OFFLINE_MODE = booleanPreferencesKey("ai_offline_mode")
        private val AI_LEARNING_ENABLED = booleanPreferencesKey("ai_learning_enabled")
        private val AI_CONTEXT_MEMORY = intPreferencesKey("ai_context_memory")

        // Configuración de llamadas
        private val AUTO_ANSWER_CALLS = booleanPreferencesKey("auto_answer_calls")
        private val AUTO_ANSWER_FAVORITES_ONLY = booleanPreferencesKey("auto_answer_favorites_only")
        private val CALL_RECORDING = booleanPreferencesKey("call_recording")
        private val CALL_TRANSCRIPTION = booleanPreferencesKey("call_transcription")
        private val CALL_SUMMARY = booleanPreferencesKey("call_summary")
        private val SPEAKER_ENABLED = booleanPreferencesKey("speaker_enabled")

        // Configuración de mensajes
        private val AUTO_READ_MESSAGES = booleanPreferencesKey("auto_read_messages")
        private val AUTO_REPLY_MESSAGES = booleanPreferencesKey("auto_reply_messages")
        private val MESSAGE_TRANSLATION = booleanPreferencesKey("message_translation")
        private val MESSAGE_PRIORITY = booleanPreferencesKey("message_priority")

        // Configuración de voz
        private val VOICE_RECOGNITION_ENABLED = booleanPreferencesKey("voice_recognition_enabled")
        private val WAKE_WORD_ENABLED = booleanPreferencesKey("wake_word_enabled")
        private val WAKE_WORD = stringPreferencesKey("wake_word")
        private val VOICE_SENSITIVITY = floatPreferencesKey("voice_sensitivity")

        // Configuración de privacidad
        private val PRIVACY_MODE = booleanPreferencesKey("privacy_mode")
        private val DATA_COLLECTION = booleanPreferencesKey("data_collection")
        private val ANALYTICS_ENABLED