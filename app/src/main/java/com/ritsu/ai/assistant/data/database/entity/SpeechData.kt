package com.ritsu.ai.assistant.data.database.entity

import androidx.room.*
import java.time.LocalDateTime

@Entity(
    tableName = "speech_data",
    indices = [
        Index(value = ["timestamp"]),
        Index(value = ["type"]),
        Index(value = ["language"])
    ]
)
data class SpeechData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "type")
    val type: String, // "recognition", "synthesis", "translation"
    
    @ColumnInfo(name = "content")
    val content: String,
    
    @ColumnInfo(name = "language")
    val language: String = "es",
    
    @ColumnInfo(name = "confidence_score")
    val confidenceScore: Float? = null,
    
    @ColumnInfo(name = "duration_seconds")
    val durationSeconds: Float? = null,
    
    @ColumnInfo(name = "audio_file_path")
    val audioFilePath: String? = null,
    
    @ColumnInfo(name = "model_used")
    val modelUsed: String? = null,
    
    @ColumnInfo(name = "processing_time_ms")
    val processingTimeMs: Long? = null,
    
    @ColumnInfo(name = "is_successful")
    val isSuccessful: Boolean = true,
    
    @ColumnInfo(name = "error_message")
    val errorMessage: String? = null,
    
    @ColumnInfo(name = "timestamp")
    val timestamp: LocalDateTime = LocalDateTime.now(),
    
    @ColumnInfo(name = "metadata")
    val metadata: String? = null
)