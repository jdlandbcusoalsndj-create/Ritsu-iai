package com.ritsu.ai.assistant.data.database.dao

import androidx.room.*
import com.ritsu.ai.assistant.data.database.entity.AIModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface AIModelDao {
    
    @Query("SELECT * FROM ai_models ORDER BY created_at DESC")
    fun getAllModels(): Flow<List<AIModel>>
    
    @Query("SELECT * FROM ai_models WHERE id = :id")
    suspend fun getModelById(id: Long): AIModel?
    
    @Query("SELECT * FROM ai_models WHERE model_name = :modelName")
    suspend fun getModelByName(modelName: String): AIModel?
    
    @Query("SELECT * FROM ai_models WHERE model_type = :modelType ORDER BY created_at DESC")
    fun getModelsByType(modelType: String): Flow<List<AIModel>>
    
    @Query("SELECT * FROM ai_models WHERE is_active = 1")
    fun getActiveModels(): Flow<List<AIModel>>
    
    @Query("SELECT * FROM ai_models WHERE is_downloaded = 1 ORDER BY created_at DESC")
    fun getDownloadedModels(): Flow<List<AIModel>>
    
    @Query("SELECT * FROM ai_models WHERE is_downloaded = 0 ORDER BY created_at DESC")
    fun getNotDownloadedModels(): Flow<List<AIModel>>
    
    @Query("SELECT * FROM ai_models WHERE supported_languages LIKE '%' || :language || '%'")
    fun getModelsByLanguage(language: String): Flow<List<AIModel>>
    
    @Query("SELECT * FROM ai_models WHERE last_used >= :date ORDER BY last_used DESC")
    fun getRecentlyUsedModels(date: LocalDateTime): Flow<List<AIModel>>
    
    @Query("SELECT * FROM ai_models ORDER BY usage_count DESC LIMIT :limit")
    suspend fun getMostUsedModels(limit: Int): List<AIModel>
    
    @Query("SELECT COUNT(*) FROM ai_models")
    suspend fun getModelCount(): Int
    
    @Query("SELECT COUNT(*) FROM ai_models WHERE is_downloaded = 1")
    suspend fun getDownloadedModelCount(): Int
    
    @Query("SELECT COUNT(*) FROM ai_models WHERE is_active = 1")
    suspend fun getActiveModelCount(): Int
    
    @Query("SELECT SUM(file_size_bytes) FROM ai_models WHERE is_downloaded = 1")
    suspend fun getTotalDownloadedSize(): Long?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModel(model: AIModel): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModels(models: List<AIModel>)
    
    @Update
    suspend fun updateModel(model: AIModel)
    
    @Delete
    suspend fun deleteModel(model: AIModel)
    
    @Query("DELETE FROM ai_models WHERE id = :id")
    suspend fun deleteModelById(id: Long)
    
    @Query("DELETE FROM ai_models")
    suspend fun deleteAllModels()
    
    @Query("UPDATE ai_models SET is_active = :isActive WHERE id = :id")
    suspend fun updateActiveStatus(id: Long, isActive: Boolean)
    
    @Query("UPDATE ai_models SET is_downloaded = :isDownloaded WHERE id = :id")
    suspend fun updateDownloadedStatus(id: Long, isDownloaded: Boolean)
    
    @Query("UPDATE ai_models SET download_progress = :progress WHERE id = :id")
    suspend fun updateDownloadProgress(id: Long, progress: Int)
    
    @Query("UPDATE ai_models SET file_path = :path WHERE id = :id")
    suspend fun updateFilePath(id: Long, path: String)
    
    @Query("UPDATE ai_models SET last_used = :date WHERE id = :id")
    suspend fun updateLastUsed(id: Long, date: LocalDateTime)
    
    @Query("UPDATE ai_models SET usage_count = usage_count + 1 WHERE id = :id")
    suspend fun incrementUsageCount(id: Long)
    
    @Query("UPDATE ai_models SET performance_score = :score WHERE id = :id")
    suspend fun updatePerformanceScore(id: Long, score: Float)
    
    @Query("UPDATE ai_models SET model_config = :config WHERE id = :id")
    suspend fun updateModelConfig(id: Long, config: String)
    
    @Query("UPDATE ai_models SET supported_languages = :languages WHERE id = :id")
    suspend fun updateSupportedLanguages(id: Long, languages: List<String>)
    
    @Query("SELECT DISTINCT model_type FROM ai_models")
    suspend fun getAvailableModelTypes(): List<String>
    
    @Query("SELECT DISTINCT supported_languages FROM ai_models")
    suspend fun getAvailableLanguages(): List<String>
    
    @Query("SELECT * FROM ai_models WHERE model_name LIKE '%' || :keyword || '%' ORDER BY created_at DESC")
    fun searchModels(keyword: String): Flow<List<AIModel>>
    
    @Query("SELECT * FROM ai_models WHERE file_size_bytes <= :maxSize ORDER BY created_at DESC")
    fun getModelsByMaxSize(maxSize: Long): Flow<List<AIModel>>
    
    @Query("SELECT * FROM ai_models WHERE performance_score >= :minScore ORDER BY performance_score DESC")
    fun getModelsByMinPerformance(minScore: Float): Flow<List<AIModel>>
}