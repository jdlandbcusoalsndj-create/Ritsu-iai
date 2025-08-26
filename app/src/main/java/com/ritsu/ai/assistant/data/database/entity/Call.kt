package com.ritsu.ai.assistant.data.database.entity

import androidx.room.*
import com.ritsu.ai.assistant.data.database.CallType
import java.time.LocalDateTime

@Entity(
    tableName = "calls",
    indices = [
        Index(value = ["timestamp"]),
        Index(value = ["type"]),
        Index(value = ["contact_id"]),
        Index(value = ["phone_number"])
    ]
)
data class Call(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "type")
    val type: CallType,
    
    @ColumnInfo(name = "contact_id")
    val contactId: Long? = null,
    
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,
    
    @ColumnInfo(name = "timestamp")
    val timestamp: LocalDateTime = LocalDateTime.now(),
    
    @ColumnInfo(name = "duration_seconds")
    val durationSeconds: Int = 0,
    
    @ColumnInfo(name = "was_answered_by_ritsu")
    val wasAnsweredByRitsu: Boolean = false,
    
    @ColumnInfo(name = "ritsu_conversation_summary")
    val ritsuConversationSummary: String? = null,
    
    @ColumnInfo(name = "transcription")
    val transcription: String? = null,
    
    @ColumnInfo(name = "sentiment_score")
    val sentimentScore: Float? = null,
    
    @ColumnInfo(name = "priority")
    val priority: Int = 0,
    
    @ColumnInfo(name = "is_spam")
    val isSpam: Boolean = false,
    
    @ColumnInfo(name = "spam_confidence")
    val spamConfidence: Float? = null,
    
    @ColumnInfo(name = "notes")
    val notes: String? = null,
    
    @ColumnInfo(name = "recording_path")
    val recordingPath: String? = null,
    
    @ColumnInfo(name = "external_id")
    val externalId: String? = null,
    
    @ColumnInfo(name = "tags")
    val tags: List<String> = emptyList()
)