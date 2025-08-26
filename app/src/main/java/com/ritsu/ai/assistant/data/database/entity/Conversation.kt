package com.ritsu.ai.assistant.data.database.entity

import androidx.room.*
import com.ritsu.ai.assistant.data.database.ConversationType
import java.time.LocalDateTime

@Entity(
    tableName = "conversations",
    indices = [
        Index(value = ["timestamp"]),
        Index(value = ["type"]),
        Index(value = ["contact_id"])
    ]
)
data class Conversation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "type")
    val type: ConversationType,
    
    @ColumnInfo(name = "contact_id")
    val contactId: Long? = null,
    
    @ColumnInfo(name = "user_message")
    val userMessage: String,
    
    @ColumnInfo(name = "ritsu_response")
    val ritsuResponse: String,
    
    @ColumnInfo(name = "timestamp")
    val timestamp: LocalDateTime = LocalDateTime.now(),
    
    @ColumnInfo(name = "duration_seconds")
    val durationSeconds: Int? = null,
    
    @ColumnInfo(name = "language")
    val language: String = "es",
    
    @ColumnInfo(name = "sentiment_score")
    val sentimentScore: Float? = null,
    
    @ColumnInfo(name = "context_data")
    val contextData: String? = null,
    
    @ColumnInfo(name = "is_processed")
    val isProcessed: Boolean = false,
    
    @ColumnInfo(name = "tags")
    val tags: List<String> = emptyList()
)