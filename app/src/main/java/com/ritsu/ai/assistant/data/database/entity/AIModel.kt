package com.ritsu.ai.assistant.data.database.entity

import androidx.room.*
import java.time.LocalDateTime

@Entity(
    tableName = "ai_models",
    indices = [
        Index(value = ["model_name"]),
        Index(value = ["model_type"]),
        Index(value = ["is_active"])
    ]
)
data class AIModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "model_name")
    val modelName: String,
    
    @ColumnInfo(name = "model_type")
    val modelType: String, // "llama", "whisper", "tts", "sentiment"
    
    @ColumnInfo(name = "version")
    val version: String,
    
    @ColumnInfo(name = "file_path")
    val filePath: String,
    
    @ColumnInfo(name = "file_size_bytes")
    val fileSizeBytes: Long,
    
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = false,
    
    @ColumnInfo(name = "is_downloaded")
    val isDownloaded: Boolean = false,
    
    @ColumnInfo(name = "download_progress")
    val downloadProgress: Int = 0,
    
    @ColumnInfo(name = "model_config")
    val modelConfig: String? = null,
    
    @ColumnInfo(name = "supported_languages")
    val supportedLanguages: List<String> = listOf("es"),
    
    @ColumnInfo(name = "performance_score")
    val performanceScore: Float? = null,
    
    @ColumnInfo(name = "last_used")
    val lastUsed: LocalDateTime? = null,
    
    @ColumnInfo(name = "usage_count")
    val usageCount: Int = 0,
    
    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)