package com.ritsu.ai.assistant.data.database.entity

import androidx.room.*
import com.ritsu.ai.assistant.data.database.MessageType
import java.time.LocalDateTime

@Entity(
    tableName = "messages",
    indices = [
        Index(value = ["timestamp"]),
        Index(value = ["type"]),
        Index(value = ["contact_id"]),
        Index(value = ["is_read"])
    ]
)
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "type")
    val type: MessageType,
    
    @ColumnInfo(name = "contact_id")
    val contactId: Long? = null,
    
    @ColumnInfo(name = "sender_number")
    val senderNumber: String? = null,
    
    @ColumnInfo(name = "content")
    val content: String,
    
    @ColumnInfo(name = "timestamp")
    val timestamp: LocalDateTime = LocalDateTime.now(),
    
    @ColumnInfo(name = "is_incoming")
    val isIncoming: Boolean = true,
    
    @ColumnInfo(name = "is_read")
    val isRead: Boolean = false,
    
    @ColumnInfo(name = "is_processed_by_ritsu")
    val isProcessedByRitsu: Boolean = false,
    
    @ColumnInfo(name = "ritsu_response")
    val ritsuResponse: String? = null,
    
    @ColumnInfo(name = "language")
    val language: String = "es",
    
    @ColumnInfo(name = "sentiment_score")
    val sentimentScore: Float? = null,
    
    @ColumnInfo(name = "priority")
    val priority: Int = 0,
    
    @ColumnInfo(name = "attachments")
    val attachments: List<String> = emptyList(),
    
    @ColumnInfo(name = "thread_id")
    val threadId: String? = null,
    
    @ColumnInfo(name = "external_id")
    val externalId: String? = null,
    
    @ColumnInfo(name = "tags")
    val tags: List<String> = emptyList()
)