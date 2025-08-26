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
        private val ANALYTICS_ENABLED = booleanPreferencesKey("analytics_enabled")
        
        // Configuración de rendimiento
        private val BATTERY_OPTIMIZATION = booleanPreferencesKey("battery_optimization")
        private val BACKGROUND_PROCESSING = booleanPreferencesKey("background_processing")
        private val CACHE_SIZE = intPreferencesKey("cache_size")
        
        // Configuración de notificaciones
        private val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        private val NOTIFICATION_SOUND = booleanPreferencesKey("notification_sound")
        private val NOTIFICATION_VIBRATION = booleanPreferencesKey("notification_vibration")
        private val NOTIFICATION_PRIORITY = stringPreferencesKey("notification_priority")
    }
    
    // Flujos de preferencias
    val isFirstLaunch: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_FIRST_LAUNCH] ?: true
    }
    
    val themeMode: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME_MODE] ?: "auto"
    }
    
    val language: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LANGUAGE] ?: "es"
    }
    
    val ritsuPersonality: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[RITSU_PERSONALITY] ?: "friendly"
    }
    
    val ritsuVoiceSpeed: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[RITSU_VOICE_SPEED] ?: 1.0f
    }
    
    val ritsuVoicePitch: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[RITSU_VOICE_PITCH] ?: 1.0f
    }
    
    val ritsuVoiceLanguage: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[RITSU_VOICE_LANGUAGE] ?: "es"
    }
    
    val aiModelType: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[AI_MODEL_TYPE] ?: "llama"
    }
    
    val aiOfflineMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[AI_OFFLINE_MODE] ?: true
    }
    
    val aiLearningEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[AI_LEARNING_ENABLED] ?: true
    }
    
    val aiContextMemory: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[AI_CONTEXT_MEMORY] ?: 10
    }
    
    val autoAnswerCalls: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[AUTO_ANSWER_CALLS] ?: false
    }
    
    val autoAnswerFavoritesOnly: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[AUTO_ANSWER_FAVORITES_ONLY] ?: true
    }
    
    val callRecording: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[CALL_RECORDING] ?: false
    }
    
    val callTranscription: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[CALL_TRANSCRIPTION] ?: true
    }
    
    val callSummary: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[CALL_SUMMARY] ?: true
    }
    
    val speakerEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[SPEAKER_ENABLED] ?: false
    }
    
    val autoReadMessages: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[AUTO_READ_MESSAGES] ?: true
    }
    
    val autoReplyMessages: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[AUTO_REPLY_MESSAGES] ?: false
    }
    
    val messageTranslation: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[MESSAGE_TRANSLATION] ?: false
    }
    
    val messagePriority: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[MESSAGE_PRIORITY] ?: true
    }
    
    val voiceRecognitionEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[VOICE_RECOGNITION_ENABLED] ?: true
    }
    
    val wakeWordEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[WAKE_WORD_ENABLED] ?: true
    }
    
    val wakeWord: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[WAKE_WORD] ?: "hey ritsu"
    }
    
    val voiceSensitivity: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[VOICE_SENSITIVITY] ?: 0.8f
    }
    
    val privacyMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PRIVACY_MODE] ?: false
    }
    
    val dataCollection: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DATA_COLLECTION] ?: true
    }
    
    val analyticsEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[ANALYTICS_ENABLED] ?: false
    }
    
    val batteryOptimization: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[BATTERY_OPTIMIZATION] ?: true
    }
    
    val backgroundProcessing: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[BACKGROUND_PROCESSING] ?: true
    }
    
    val cacheSize: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[CACHE_SIZE] ?: 100
    }
    
    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_ENABLED] ?: true
    }
    
    val notificationSound: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATION_SOUND] ?: true
    }
    
    val notificationVibration: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATION_VIBRATION] ?: true
    }
    
    val notificationPriority: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATION_PRIORITY] ?: "high"
    }
    
    // Métodos para actualizar preferencias
    suspend fun setFirstLaunch(isFirstLaunch: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_FIRST_LAUNCH] = isFirstLaunch
        }
    }
    
    suspend fun setThemeMode(themeMode: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE] = themeMode
        }
    }
    
    suspend fun setLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE] = language
        }
    }
    
    suspend fun setRitsuPersonality(personality: String) {
        context.dataStore.edit { preferences ->
            preferences[RITSU_PERSONALITY] = personality
        }
    }
    
    suspend fun setRitsuVoiceSpeed(speed: Float) {
        context.dataStore.edit { preferences ->
            preferences[RITSU_VOICE_SPEED] = speed
        }
    }
    
    suspend fun setRitsuVoicePitch(pitch: Float) {
        context.dataStore.edit { preferences ->
            preferences[RITSU_VOICE_PITCH] = pitch
        }
    }
    
    suspend fun setRitsuVoiceLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[RITSU_VOICE_LANGUAGE] = language
        }
    }
    
    suspend fun setAiModelType(modelType: String) {
        context.dataStore.edit { preferences ->
            preferences[AI_MODEL_TYPE] = modelType
        }
    }
    
    suspend fun setAiOfflineMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AI_OFFLINE_MODE] = enabled
        }
    }
    
    suspend fun setAiLearningEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AI_LEARNING_ENABLED] = enabled
        }
    }
    
    suspend fun setAiContextMemory(memory: Int) {
        context.dataStore.edit { preferences ->
            preferences[AI_CONTEXT_MEMORY] = memory
        }
    }
    
    suspend fun setAutoAnswerCalls(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_ANSWER_CALLS] = enabled
        }
    }
    
    suspend fun setAutoAnswerFavoritesOnly(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_ANSWER_FAVORITES_ONLY] = enabled
        }
    }
    
    suspend fun setCallRecording(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[CALL_RECORDING] = enabled
        }
    }
    
    suspend fun setCallTranscription(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[CALL_TRANSCRIPTION] = enabled
        }
    }
    
    suspend fun setCallSummary(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[CALL_SUMMARY] = enabled
        }
    }
    
    suspend fun setSpeakerEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SPEAKER_ENABLED] = enabled
        }
    }
    
    suspend fun setAutoReadMessages(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_READ_MESSAGES] = enabled
        }
    }
    
    suspend fun setAutoReplyMessages(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_REPLY_MESSAGES] = enabled
        }
    }
    
    suspend fun setMessageTranslation(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[MESSAGE_TRANSLATION] = enabled
        }
    }
    
    suspend fun setMessagePriority(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[MESSAGE_PRIORITY] = enabled
        }
    }
    
    suspend fun setVoiceRecognitionEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[VOICE_RECOGNITION_ENABLED] = enabled
        }
    }
    
    suspend fun setWakeWordEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[WAKE_WORD_ENABLED] = enabled
        }
    }
    
    suspend fun setWakeWord(word: String) {
        context.dataStore.edit { preferences ->
            preferences[WAKE_WORD] = word
        }
    }
    
    suspend fun setVoiceSensitivity(sensitivity: Float) {
        context.dataStore.edit { preferences ->
            preferences[VOICE_SENSITIVITY] = sensitivity
        }
    }
    
    suspend fun setPrivacyMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PRIVACY_MODE] = enabled
        }
    }
    
    suspend fun setDataCollection(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DATA_COLLECTION] = enabled
        }
    }
    
    suspend fun setAnalyticsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ANALYTICS_ENABLED] = enabled
        }
    }
    
    suspend fun setBatteryOptimization(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[BATTERY_OPTIMIZATION] = enabled
        }
    }
    
    suspend fun setBackgroundProcessing(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[BACKGROUND_PROCESSING] = enabled
        }
    }
    
    suspend fun setCacheSize(size: Int) {
        context.dataStore.edit { preferences ->
            preferences[CACHE_SIZE] = size
        }
    }
    
    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED] = enabled
        }
    }
    
    suspend fun setNotificationSound(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_SOUND] = enabled
        }
    }
    
    suspend fun setNotificationVibration(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_VIBRATION] = enabled
        }
    }
    
    suspend fun setNotificationPriority(priority: String) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_PRIORITY] = priority
        }
    }
    
    // Métodos de conveniencia
    suspend fun isSpeakerEnabled(): Boolean {
        return speakerEnabled.first()
    }
    
    suspend fun isAutoAnswerEnabled(): Boolean {
        return autoAnswerCalls.first()
    }
    
    suspend fun isVoiceRecognitionEnabled(): Boolean {
        return voiceRecognitionEnabled.first()
    }
    
    suspend fun isWakeWordEnabled(): Boolean {
        return wakeWordEnabled.first()
    }
    
    suspend fun getCurrentWakeWord(): String {
        return wakeWord.first()
    }
    
    suspend fun isPrivacyModeEnabled(): Boolean {
        return privacyMode.first()
    }
    
    suspend fun isOfflineModeEnabled(): Boolean {
        return aiOfflineMode.first()
    }
    
    suspend fun isBatteryOptimizationEnabled(): Boolean {
        return batteryOptimization.first()
    }
    
    suspend fun isNotificationsEnabled(): Boolean {
        return notificationsEnabled.first()
    }
}