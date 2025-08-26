package com.ritsu.ai.assistant.data.database.entity

import androidx.room.*
import java.time.LocalDateTime

@Entity(
    tableName = "contacts",
    indices = [
        Index(value = ["phone_number"]),
        Index(value = ["email"]),
        Index(value = ["name"])
    ]
)
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String? = null,
    
    @ColumnInfo(name = "email")
    val email: String? = null,
    
    @ColumnInfo(name = "avatar_uri")
    val avatarUri: String? = null,
    
    @ColumnInfo(name = "relationship")
    val relationship: String? = null,
    
    @ColumnInfo(name = "ritsu_personality")
    val ritsuPersonality: String = "friendly",
    
    @ColumnInfo(name = "auto_answer_enabled")
    val autoAnswerEnabled: Boolean = false,
    
    @ColumnInfo(name = "custom_greeting")
    val customGreeting: String? = null,
    
    @ColumnInfo(name = "custom_farewell")
    val customFarewell: String? = null,
    
    @ColumnInfo(name = "important_notes")
    val importantNotes: String? = null,
    
    @ColumnInfo(name = "preferred_language")
    val preferredLanguage: String = "es",
    
    @ColumnInfo(name = "last_interaction")
    val lastInteraction: LocalDateTime? = null,
    
    @ColumnInfo(name = "interaction_count")
    val interactionCount: Int = 0,
    
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,
    
    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    
    @ColumnInfo(name = "tags")
    val tags: List<String> = emptyList()
)