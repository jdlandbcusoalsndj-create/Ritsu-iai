package com.ritsu.ai.assistant.data.repository

import android.content.Context
import com.ritsu.ai.assistant.data.database.dao.AIModelDao
import com.ritsu.ai.assistant.data.database.entity.AIModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class AIModelRepository(private val context: Context) {
    
    private val aiModelDao = RitsuApplication.database.aiModelDao()
    
    fun getAllModels(): Flow<List<AIModel>> = aiModelDao.getAllModels()
    
    suspend fun getModelById(id: Long): AIModel? = aiModelDao.getModelById(id)
    
    suspend fun getModelByName(modelName: String): AIModel? = aiModelDao.getModelByName(modelName)
    
    fun getModelsByType(modelType: String): Flow<List<AIModel>> = aiModelDao.getModelsByType(modelType)
    
    fun getActiveModels(): Flow<List<AIModel>> = aiModelDao.getActiveModels()
    
    fun getDownloadedModels(): Flow<List<AIModel>> = aiModelDao.getDownloadedModels()
    
    fun getNotDownloadedModels(): Flow<List<AIModel>> = aiModelDao.getNotDownloadedModels()
    
    fun getModelsByLanguage(language: String): Flow<List<AIModel>> = 
        aiModelDao.getModelsByLanguage(language)
    
    fun getRecentlyUsedModels(date: LocalDateTime): Flow<List<AIModel>> = 
        aiModelDao.getRecentlyUsedModels(date)
    
    suspend fun getMostUsedModels(limit: Int): List<AIModel> = aiModelDao.getMostUsedModels(limit)
    
    suspend fun getModelCount(): Int = aiModelDao.getModelCount()
    
    suspend fun getDownloadedModelCount(): Int = aiModelDao.getDownloadedModelCount()
    
    suspend fun getActiveModelCount(): Int = aiModelDao.getActiveModelCount()
    
    suspend fun getTotalDownloadedSize(): Long? = aiModelDao.getTotalDownloadedSize()
    
    suspend fun insertModel(model: AIModel): Long = aiModelDao.insertModel(model)
    
    suspend fun insertModels(models: List<AIModel>) = aiModelDao.insertModels(models)
    
    suspend fun updateModel(model: AIModel) = aiModelDao.updateModel(model)
    
    suspend fun deleteModel(model: AIModel) = aiModelDao.deleteModel(model)
    
    suspend fun deleteModelById(id: Long) = aiModelDao.deleteModelById(id)
    
    suspend fun deleteAllModels() = aiModelDao.deleteAllModels()
    
    suspend fun updateActiveStatus(id: Long, isActive: Boolean) = 
        aiModelDao.updateActiveStatus(id, isActive)
    
    suspend fun updateDownloadedStatus(id: Long, isDownloaded: Boolean) = 
        aiModelDao.updateDownloadedStatus(id, isDownloaded)
    
    suspend fun updateDownloadProgress(id: Long, progress: Int) = 
        aiModelDao.updateDownloadProgress(id, progress)
    
    suspend fun updateFilePath(id: Long, path: String) = aiModelDao.updateFilePath(id, path)
    
    suspend fun updateLastUsed(id: Long, date: LocalDateTime) = aiModelDao.updateLastUsed(id, date)
    
    suspend fun incrementUsageCount(id: Long) = aiModelDao.incrementUsageCount(id)
    
    suspend fun updatePerformanceScore(id: Long, score: Float) = 
        aiModelDao.updatePerformanceScore(id, score)
    
    suspend fun updateModelConfig(id: Long, config: String) = aiModelDao.updateModelConfig(id, config)
    
    suspend fun updateSupportedLanguages(id: Long, languages: List<String>) = 
        aiModelDao.updateSupportedLanguages(id, languages)
    
    suspend fun getAvailableModelTypes(): List<String> = aiModelDao.getAvailableModelTypes()
    
    suspend fun getAvailableLanguages(): List<String> = aiModelDao.getAvailableLanguages()
    
    fun searchModels(keyword: String): Flow<List<AIModel>> = aiModelDao.searchModels(keyword)
    
    fun getModelsByMaxSize(maxSize: Long): Flow<List<AIModel>> = aiModelDao.getModelsByMaxSize(maxSize)
    
    fun getModelsByMinPerformance(minScore: Float): Flow<List<AIModel>> = 
        aiModelDao.getModelsByMinPerformance(minScore)
    
    // Métodos específicos para gestión de modelos
    suspend fun getModelFile(model: AIModel): java.io.File? {
        return if (model.isDownloaded && model.filePath.isNotEmpty()) {
            java.io.File(model.filePath)
        } else {
            null
        }
    }
    
    suspend fun isModelDownloaded(modelName: String): Boolean {
        val model = getModelByName(modelName)
        return model?.isDownloaded == true
    }
    
    suspend fun getActiveModelByType(modelType: String): AIModel? {
        return getActiveModels().first().find { it.modelType == modelType }
    }
    
    suspend fun setActiveModel(modelId: Long) {
        // Desactivar todos los modelos del mismo tipo
        val model = getModelById(modelId)
        model?.let { currentModel ->
            getModelsByType(currentModel.modelType).first().forEach { modelOfSameType ->
                if (modelOfSameType.id != modelId) {
                    updateActiveStatus(modelOfSameType.id, false)
                }
            }
            // Activar el modelo seleccionado
            updateActiveStatus(modelId, true)
        }
    }
}