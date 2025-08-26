package com.ritsu.ai.assistant.data.database.dao

import androidx.room.*
import com.ritsu.ai.assistant.data.database.entity.SpeechData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface SpeechDataDao {
    
    @Query("SELECT * FROM speech_data ORDER BY timestamp DESC")
    fun getAllSpeechData(): Flow<List<SpeechData>>
    
    @Query("SELECT * FROM speech_data WHERE id = :id")
    suspend fun getSpeechDataById(id: Long): SpeechData?
    
    @Query("SELECT * FROM speech_data WHERE type = :type ORDER BY timestamp DESC")
    fun getSpeechDataByType(type: String): Flow<List<SpeechData>>
    
    @Query("SELECT * FROM speech_data WHERE language = :language ORDER BY timestamp DESC")
    fun getSpeechDataByLanguage(language: String): Flow<List<SpeechData>>
    
    @Query("SELECT * FROM speech_data WHERE is_successful = :isSuccessful ORDER BY timestamp DESC")
    fun getSpeechDataBySuccessStatus(isSuccessful: Boolean): Flow<List<SpeechData>>
    
    @Query("SELECT * FROM speech_data WHERE timestamp >= :startDate ORDER BY timestamp DESC")
    fun getSpeechDataFromDate(startDate: LocalDateTime): Flow<List<SpeechData>>
    
    @Query("SELECT * FROM speech_data WHERE model_used = :model ORDER BY timestamp DESC")
    fun getSpeechDataByModel(model: String): Flow<List<SpeechData>>
    
    @Query("SELECT * FROM speech_data WHERE confidence_score >= :minConfidence ORDER BY timestamp DESC")
    fun getSpeechDataByMinConfidence(minConfidence: Float): Flow<List<SpeechData>>
    
    @Query("SELECT * FROM speech_data WHERE duration_seconds >= :minDuration ORDER BY timestamp DESC")
    fun getSpeechDataByMinDuration(minDuration: Float): Flow<List<SpeechData>>
    
    @Query("SELECT * FROM speech_data ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentSpeechData(limit: Int): List<SpeechData>
    
    @Query("SELECT COUNT(*) FROM speech_data WHERE timestamp >= :startDate")
    suspend fun getSpeechDataCountFromDate(startDate: LocalDateTime): Int
    
    @Query("SELECT COUNT(*) FROM speech_data WHERE type = :type")
    suspend fun getSpeechDataCountByType(type: String): Int
    
    @Query("SELECT COUNT(*) FROM speech_data WHERE is_successful = 1")
    suspend fun getSuccessfulSpeechDataCount(): Int
    
    @Query("SELECT AVG(confidence_score) FROM speech_data WHERE confidence_score IS NOT NULL")
    suspend fun getAverageConfidenceScore(): Float?
    
    @Query("SELECT AVG(duration_seconds) FROM speech_data WHERE duration_seconds IS NOT NULL")
    suspend fun getAverageDuration(): Float?
    
    @Query("SELECT AVG(processing_time_ms) FROM speech_data WHERE processing_time_ms IS NOT NULL")
    suspend fun getAverageProcessingTime(): Float?
    
    @Query("SELECT * FROM speech_data WHERE type = :type ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastSpeechDataByType(type: String): SpeechData?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpeechData(speechData: SpeechData): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpeechDataList(speechDataList: List<SpeechData>)
    
    @Update
    suspend fun updateSpeechData(speechData: SpeechData)
    
    @Delete
    suspend fun deleteSpeechData(speechData: SpeechData)
    
    @Query("DELETE FROM speech_data WHERE id = :id")
    suspend fun deleteSpeechDataById(id: Long)
    
    @Query("DELETE FROM speech_data WHERE timestamp < :date")
    suspend fun deleteSpeechDataOlderThan(date: LocalDateTime)
    
    @Query("DELETE FROM speech_data")
    suspend fun deleteAllSpeechData()
    
    // Métodos para actualizar campos específicos
    @Query("UPDATE speech_data SET confidence_score = :score WHERE id = :id")
    suspend fun updateConfidenceScore(id: Long, score: Float)
    
    @Query("UPDATE speech_data SET duration_seconds = :duration WHERE id = :id")
    suspend fun updateDuration(id: Long, duration: Float)
    
    @Query("UPDATE speech_data SET audio_file_path = :path WHERE id = :id")
    suspend fun updateAudioFilePath(id: Long, path: String)
    
    @Query("UPDATE speech_data SET model_used = :model WHERE id = :id")
    suspend fun updateModelUsed(id: Long, model: String)
    
    @Query("UPDATE speech_data SET processing_time_ms = :time WHERE id = :id")
    suspend fun updateProcessingTime(id: Long, time: Long)
    
    @Query("UPDATE speech_data SET is_successful = :success WHERE id = :id")
    suspend fun updateSuccessStatus(id: Long, success: Boolean)
    
    @Query("UPDATE speech_data SET error_message = :error WHERE id = :id")
    suspend fun updateErrorMessage(id: Long, error: String)
    
    @Query("UPDATE speech_data SET metadata = :metadata WHERE id = :id")
    suspend fun updateMetadata(id: Long, metadata: String)
    
    // Métodos de consulta específicos
    @Query("SELECT * FROM speech_data WHERE content LIKE '%' || :keyword || '%' ORDER BY timestamp DESC")
    fun searchSpeechData(keyword: String): Flow<List<SpeechData>>
    
    @Query("SELECT * FROM speech_data WHERE error_message IS NOT NULL ORDER BY timestamp DESC")
    fun getSpeechDataWithErrors(): Flow<List<SpeechData>>
    
    @Query("SELECT * FROM speech_data WHERE audio_file_path IS NOT NULL ORDER BY timestamp DESC")
    fun getSpeechDataWithAudioFiles(): Flow<List<SpeechData>>
    
    @Query("SELECT DISTINCT type FROM speech_data ORDER BY type")
    suspend fun getAvailableTypes(): List<String>
    
    @Query("SELECT DISTINCT language FROM speech_data ORDER BY language")
    suspend fun getAvailableLanguages(): List<String>
    
    @Query("SELECT DISTINCT model_used FROM speech_data WHERE model_used IS NOT NULL ORDER BY model_used")
    suspend fun getAvailableModels(): List<String>
    
    // Métodos para estadísticas
    @Query("SELECT type, COUNT(*) as count FROM speech_data GROUP BY type")
    suspend fun getSpeechDataCountByType(): Map<String, Int>
    
    @Query("SELECT language, COUNT(*) as count FROM speech_data GROUP BY language")
    suspend fun getSpeechDataCountByLanguage(): Map<String, Int>
    
    @Query("SELECT is_successful, COUNT(*) as count FROM speech_data GROUP BY is_successful")
    suspend fun getSpeechDataCountBySuccess(): Map<Boolean, Int>
    
    // Métodos para análisis de rendimiento
    @Query("SELECT AVG(processing_time_ms) FROM speech_data WHERE type = :type AND processing_time_ms IS NOT NULL")
    suspend fun getAverageProcessingTimeByType(type: String): Float?
    
    @Query("SELECT AVG(confidence_score) FROM speech_data WHERE type = :type AND confidence_score IS NOT NULL")
    suspend fun getAverageConfidenceByType(type: String): Float?
    
    @Query("SELECT AVG(duration_seconds) FROM speech_data WHERE type = :type AND duration_seconds IS NOT NULL")
    suspend fun getAverageDurationByType(type: String): Float?
}