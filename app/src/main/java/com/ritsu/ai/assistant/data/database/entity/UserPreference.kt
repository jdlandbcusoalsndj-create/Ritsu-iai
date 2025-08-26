package com.ritsu.ai.assistant.data.database.entity

import androidx.room.*
import java.time.LocalDateTime

@Entity(
    tableName = "user_preferences",
    indices = [
        Index(value = ["preference_key"], unique = true)
    ]
)
data class UserPreference(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "preference_key")
    val preferenceKey: String,
    
    @ColumnInfo(name = "preference_value")
    val preferenceValue: String,
    
    @ColumnInfo(name = "preference_type")
    val preferenceType: String = "string", // string, int, boolean, float
    
    @ColumnInfo(name = "category")
    val category: String = "general",
    
    @ColumnInfo(name = "description")
    val description: String? = null,
    
    @ColumnInfo(name = "is_system")
    val isSystem: Boolean = false,
    
    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)