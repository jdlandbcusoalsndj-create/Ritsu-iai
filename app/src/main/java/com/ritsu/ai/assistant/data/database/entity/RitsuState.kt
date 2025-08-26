package com.ritsu.ai.assistant.data.database.entity

import androidx.room.*
import com.ritsu.ai.assistant.data.database.RitsuMood
import java.time.LocalDateTime

@Entity(
    tableName = "ritsu_states",
    indices = [
        Index(value = ["timestamp"]),
        Index(value = ["mood"])
    ]
)
data class RitsuState(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "mood")
    val mood: RitsuMood = RitsuMood.NEUTRAL,
    
    @ColumnInfo(name = "position_x")
    val positionX: Float = 0.5f,
    
    @ColumnInfo(name = "position_y")
    val positionY: Float = 0.5f,
    
    @ColumnInfo(name = "scale")
    val scale: Float = 1.0f,
    
    @ColumnInfo(name = "is_visible")
    val isVisible: Boolean = true,
    
    @ColumnInfo(name = "is_speaking")
    val isSpeaking: Boolean = false,
    
    @ColumnInfo(name = "is_listening")
    val isListening: Boolean = false,
    
    @ColumnInfo(name = "current_animation")
    val currentAnimation: String? = null,
    
    @ColumnInfo(name = "energy_level")
    val energyLevel: Float = 1.0f,
    
    @ColumnInfo(name = "interaction_count_today")
    val interactionCountToday: Int = 0,
    
    @ColumnInfo(name = "last_interaction")
    val lastInteraction: LocalDateTime? = null,
    
    @ColumnInfo(name = "current_context")
    val currentContext: String? = null,
    
    @ColumnInfo(name = "timestamp")
    val timestamp: LocalDateTime = LocalDateTime.now(),
    
    @ColumnInfo(name = "custom_data")
    val customData: String? = null
)