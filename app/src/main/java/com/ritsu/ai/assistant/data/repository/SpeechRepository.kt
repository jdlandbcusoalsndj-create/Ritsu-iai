package com.ritsu.ai.assistant.data.repository

import com.ritsu.ai.assistant.data.database.dao.SpeechDataDao
import com.ritsu.ai.assistant.data.database.entity.SpeechData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class SpeechRepository(private val context: android.content.Context) {
    
    private val speechDataDao = RitsuApplication.database.speechDataDao()
    
    fun getAllSpeechData(): Flow<List<SpeechData>> = speechDataDao.getAllSpeechData()
    
    suspend fun getSpeechDataById(id: Long): SpeechData? = speechDataDao.getSpeechDataById(id)
    
    fun getSpeechDataByType(type: String): Flow<List<SpeechData>> = speechDataDao.getSpeechDataByType(type)
    
    fun getSpeechDataByLanguage(language: String): Flow<List<SpeechData>> = 
        speechDataDao.getSpeechDataByLanguage(language)
    
    fun getSpeechDataBySuccessStatus(isSuccessful: Boolean): Flow<List<SpeechData>> = 
        speechDataDao.getSpeechDataBySuccessStatus(isSuccessful)
    
    fun getSpeechDataFromDate(startDate: LocalDateTime): Flow<List<SpeechData>> = 
        speechDataDao.getSpeechDataFromDate(startDate)
    
    fun getSpeechDataByModel(model: String): Flow<List<SpeechData>> = 
        speechDataDao.getSpeechDataByModel(model)
    
    fun getSpeechDataByMinConfidence(minConfidence: Float): Flow<List<SpeechData>> = 
        speechDataDao.getSpeechDataByMinConfidence(minConfidence)
    
    fun getSpeechDataByMinDuration(minDuration: Float): Flow<List<SpeechData>> = 
        speechDataDao.getSpeechDataByMinDuration(minDuration)
    
    suspend fun getRecentSpeechData(limit: Int): List<SpeechData> = 
        speechDataDao.getRecentSpeechData(limit)
    
    suspend fun getSpeechDataCountFromDate(startDate: LocalDateTime): Int = 
        speechDataDao.getSpeechDataCountFromDate(startDate)
    
    suspend fun getSpeechDataCountByType(type: String): Int = 
        speechDataDao.getSpeechDataCountByType(type)
    
    suspend fun getSuccessfulSpeechDataCount(): Int = speechDataDao.getSuccessfulSpeechDataCount()
    
    suspend fun getAverageConfidenceScore(): Float? = speechDataDao.getAverageConfidenceScore()
    
    suspend fun getAverageDuration(): Float? = speechDataDao.getAverageDuration()
    
    suspend fun getAverageProcessingTime(): Float? = speechDataDao.getAverageProcessingTime()
    
    suspend fun getLastSpeechDataByType(type: String): SpeechData? = 
        speechDataDao.getLastSpeechDataByType(type)
    
    suspend fun insertSpeechData(speechData: SpeechData): Long = 
        speechDataDao.insertSpeechData(speechData)
    
    suspend fun insertSpeechDataList(speechDataList: List<SpeechData>) = 
        speechDataDao.insertSpeechDataList(speechDataList)
    
    suspend fun updateSpeechData(speechData: SpeechData) = speechDataDao.updateSpeechData(speechData)
    
    suspend fun deleteSpeechData(speechData: SpeechData) = speechDataDao.deleteSpeechData(speechData)
    
    suspend fun deleteSpeechDataById(id: Long) = speechDataDao.deleteSpeechDataById(id)
    
    suspend fun deleteSpeechDataOlderThan(date: LocalDateTime) = 
        speechDataDao.deleteSpeechDataOlderThan(date)
    
    suspend fun deleteAllSpeechData() = speechDataDao.deleteAllSpeechData()
    
    suspend fun updateConfidenceScore(id: Long, score: Float) = 
        speechDataDao.updateConfidenceScore(id, score)
    
    suspend fun updateDuration(id: Long, duration: Float) = speechDataDao.updateDuration(id, duration)
    
    suspend fun updateAudioFilePath(id: Long, path: String) = 
        speechDataDao.updateAudioFilePath(id, path)
    
    suspend fun updateModelUsed(id: Long, model: String) = speechDataDao.updateModelUsed(id, model)
    
    suspend fun updateProcessingTime(id: Long, time: Long) = 
        speechDataDao.updateProcessingTime(id, time)
    
    suspend fun updateSuccessStatus(id: Long, success: Boolean) = 
        speechDataDao.updateSuccessStatus(id, success)
    
    suspend fun updateErrorMessage(id: Long, error: String) = 
        speechDataDao.updateErrorMessage(id, error)
    
    suspend fun updateMetadata(id: Long, metadata: String) = speechDataDao.updateMetadata(id, metadata)
    
    fun searchSpeechData(keyword: String): Flow<List<SpeechData>> = 
        speechDataDao.searchSpeechData(keyword)
    
    fun getSpeechDataWithErrors(): Flow<List<SpeechData>> = speechDataDao.getSpeechDataWithErrors()
    
    fun getSpeechDataWithAudioFiles(): Flow<List<SpeechData>> = 
        speechDataDao.getSpeechDataWithAudioFiles()
    
    suspend fun getAvailableTypes(): List<String> = speechDataDao.getAvailableTypes()
    
    suspend fun getAvailableLanguages(): List<String> = speechDataDao.getAvailableLanguages()
    
    suspend fun getAvailableModels(): List<String> = speechDataDao.getAvailableModels()
    
    suspend fun getSpeechDataCountByType(): Map<String, Int> = speechDataDao.getSpeechDataCountByType()
    
    suspend fun getSpeechDataCountByLanguage(): Map<String, Int> = 
        speechDataDao.getSpeechDataCountByLanguage()
    
    suspend fun getSpeechDataCountBySuccess(): Map<Boolean, Int> = 
        speechDataDao.getSpeechDataCountBySuccess()
    
    suspend fun getAverageProcessingTimeByType(type: String): Float? = 
        speechDataDao.getAverageProcessingTimeByType(type)
    
    suspend fun getAverageConfidenceByType(type: String): Float? = 
        speechDataDao.getAverageConfidenceByType(type)
    
    suspend fun getAverageDurationByType(type: String): Float? = 
        speechDataDao.getAverageDurationByType(type)
    
    // Métodos específicos para gestión de datos de voz
    suspend fun saveRecognitionResult(
        content: String,
        language: String = "es",
        confidenceScore: Float? = null,
        modelUsed: String? = null
    ): Long {
        val speechData = SpeechData(
            type = "recognition",
            content = content,
            language = language,
            confidenceScore = confidenceScore,
            modelUsed = modelUsed,
            isSuccessful = true
        )
        return insertSpeechData(speechData)
    }
    
    suspend fun saveSynthesisResult(
        content: String,
        language: String = "es",
        durationSeconds: Float? = null,
        audioFilePath: String? = null
    ): Long {
        val speechData = SpeechData(
            type = "synthesis",
            content = content,
            language = language,
            durationSeconds = durationSeconds,
            audioFilePath = audioFilePath,
            isSuccessful = true
        )
        return insertSpeechData(speechData)
    }
    
    suspend fun saveTranslationResult(
        originalContent: String,
        translatedContent: String,
        sourceLanguage: String,
        targetLanguage: String
    ): Long {
        val speechData = SpeechData(
            type = "translation",
            content = "$originalContent -> $translatedContent",
            language = "$sourceLanguage-$targetLanguage",
            isSuccessful = true
        )
        return insertSpeechData(speechData)
    }
    
    suspend fun saveError(
        type: String,
        errorMessage: String,
        language: String = "es"
    ): Long {
        val speechData = SpeechData(
            type = type,
            content = "Error occurred",
            language = language,
            errorMessage = errorMessage,
            isSuccessful = false
        )
        return insertSpeechData(speechData)
    }
}