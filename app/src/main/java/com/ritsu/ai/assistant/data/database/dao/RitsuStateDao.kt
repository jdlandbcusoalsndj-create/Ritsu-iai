package com.ritsu.ai.assistant.data.database.dao

import androidx.room.*
import com.ritsu.ai.assistant.data.database.RitsuMood
import com.ritsu.ai.assistant.data.database.entity.RitsuState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface RitsuStateDao {
    
    @Query("SELECT * FROM ritsu_states ORDER BY timestamp DESC")
    fun getAllStates(): Flow<List<RitsuState>>
    
    @Query("SELECT * FROM ritsu_states WHERE id = :id")
    suspend fun getStateById(id: Long): RitsuState?
    
    @Query("SELECT * FROM ritsu_states ORDER BY timestamp DESC LIMIT 1")
    suspend fun getCurrentState(): RitsuState?
    
    @Query("SELECT * FROM ritsu_states WHERE mood = :mood ORDER BY timestamp DESC")
    fun getStatesByMood(mood: RitsuMood): Flow<List<RitsuState>>
    
    @Query("SELECT * FROM ritsu_states WHERE is_visible = :isVisible ORDER BY timestamp DESC")
    fun getStatesByVisibility(isVisible: Boolean): Flow<List<RitsuState>>
    
    @Query("SELECT * FROM ritsu_states WHERE is_speaking = :isSpeaking ORDER BY timestamp DESC")
    fun getStatesBySpeakingStatus(isSpeaking: Boolean): Flow<List<RitsuState>>
    
    @Query("SELECT * FROM ritsu_states WHERE is_listening = :isListening ORDER BY timestamp DESC")
    fun getStatesByListeningStatus(isListening: Boolean): Flow<List<RitsuState>>
    
    @Query("SELECT * FROM ritsu_states WHERE timestamp >= :startDate ORDER BY timestamp DESC")
    fun getStatesFromDate(startDate: LocalDateTime): Flow<List<RitsuState>>
    
    @Query("SELECT * FROM ritsu_states WHERE energy_level >= :minEnergy ORDER BY timestamp DESC")
    fun getStatesByMinEnergy(minEnergy: Float): Flow<List<RitsuState>>
    
    @Query("SELECT * FROM ritsu_states WHERE interaction_count_today >= :minInteractions ORDER BY timestamp DESC")
    fun getStatesByMinInteractions(minInteractions: Int): Flow<List<RitsuState>>
    
    @Query("SELECT * FROM ritsu_states ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentStates(limit: Int): List<RitsuState>
    
    @Query("SELECT COUNT(*) FROM ritsu_states WHERE timestamp >= :startDate")
    suspend fun getStateCountFromDate(startDate: LocalDateTime): Int
    
    @Query("SELECT AVG(energy_level) FROM ritsu_states WHERE energy_level IS NOT NULL")
    suspend fun getAverageEnergyLevel(): Float?
    
    @Query("SELECT AVG(interaction_count_today) FROM ritsu_states")
    suspend fun getAverageInteractionCount(): Float?
    
    @Query("SELECT * FROM ritsu_states WHERE mood = :mood ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastStateByMood(mood: RitsuMood): RitsuState?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertState(state: RitsuState): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStates(states: List<RitsuState>)
    
    @Update
    suspend fun updateState(state: RitsuState)
    
    @Delete
    suspend fun deleteState(state: RitsuState)
    
    @Query("DELETE FROM ritsu_states WHERE id = :id")
    suspend fun deleteStateById(id: Long)
    
    @Query("DELETE FROM ritsu_states WHERE timestamp < :date")
    suspend fun deleteStatesOlderThan(date: LocalDateTime)
    
    @Query("DELETE FROM ritsu_states")
    suspend fun deleteAllStates()
    
    // Métodos para actualizar estados específicos
    @Query("UPDATE ritsu_states SET mood = :mood WHERE id = :id")
    suspend fun updateMood(id: Long, mood: RitsuMood)
    
    @Query("UPDATE ritsu_states SET position_x = :x, position_y = :y WHERE id = :id")
    suspend fun updatePosition(id: Long, x: Float, y: Float)
    
    @Query("UPDATE ritsu_states SET scale = :scale WHERE id = :id")
    suspend fun updateScale(id: Long, scale: Float)
    
    @Query("UPDATE ritsu_states SET is_visible = :isVisible WHERE id = :id")
    suspend fun updateVisibility(id: Long, isVisible: Boolean)
    
    @Query("UPDATE ritsu_states SET is_speaking = :isSpeaking WHERE id = :id")
    suspend fun updateSpeakingStatus(id: Long, isSpeaking: Boolean)
    
    @Query("UPDATE ritsu_states SET is_listening = :isListening WHERE id = :id")
    suspend fun updateListeningStatus(id: Long, isListening: Boolean)
    
    @Query("UPDATE ritsu_states SET current_animation = :animation WHERE id = :id")
    suspend fun updateCurrentAnimation(id: Long, animation: String)
    
    @Query("UPDATE ritsu_states SET energy_level = :energy WHERE id = :id")
    suspend fun updateEnergyLevel(id: Long, energy: Float)
    
    @Query("UPDATE ritsu_states SET interaction_count_today = :count WHERE id = :id")
    suspend fun updateInteractionCount(id: Long, count: Int)
    
    @Query("UPDATE ritsu_states SET last_interaction = :date WHERE id = :id")
    suspend fun updateLastInteraction(id: Long, date: LocalDateTime)
    
    @Query("UPDATE ritsu_states SET current_context = :context WHERE id = :id")
    suspend fun updateCurrentContext(id: Long, context: String)
    
    @Query("UPDATE ritsu_states SET custom_data = :data WHERE id = :id")
    suspend fun updateCustomData(id: Long, data: String)
    
    // Métodos para el estado actual
    @Query("UPDATE ritsu_states SET mood = :mood WHERE id = (SELECT id FROM ritsu_states ORDER BY timestamp DESC LIMIT 1)")
    suspend fun updateCurrentMood(mood: RitsuMood)
    
    @Query("UPDATE ritsu_states SET position_x = :x, position_y = :y WHERE id = (SELECT id FROM ritsu_states ORDER BY timestamp DESC LIMIT 1)")
    suspend fun updateCurrentPosition(x: Float, y: Float)
    
    @Query("UPDATE ritsu_states SET is_speaking = :isSpeaking WHERE id = (SELECT id FROM ritsu_states ORDER BY timestamp DESC LIMIT 1)")
    suspend fun updateCurrentSpeakingStatus(isSpeaking: Boolean)
    
    @Query("UPDATE ritsu_states SET is_listening = :isListening WHERE id = (SELECT id FROM ritsu_states ORDER BY timestamp DESC LIMIT 1)")
    suspend fun updateCurrentListeningStatus(isListening: Boolean)
    
    @Query("UPDATE ritsu_states SET energy_level = :energy WHERE id = (SELECT id FROM ritsu_states ORDER BY timestamp DESC LIMIT 1)")
    suspend fun updateCurrentEnergyLevel(energy: Float)
    
    @Query("UPDATE ritsu_states SET interaction_count_today = interaction_count_today + 1 WHERE id = (SELECT id FROM ritsu_states ORDER BY timestamp DESC LIMIT 1)")
    suspend fun incrementCurrentInteractionCount()
    
    // Métodos de consulta específicos
    @Query("SELECT * FROM ritsu_states WHERE current_animation = :animation ORDER BY timestamp DESC")
    fun getStatesByAnimation(animation: String): Flow<List<RitsuState>>
    
    @Query("SELECT * FROM ritsu_states WHERE current_context = :context ORDER BY timestamp DESC")
    fun getStatesByContext(context: String): Flow<List<RitsuState>>
    
    @Query("SELECT * FROM ritsu_states WHERE custom_data LIKE '%' || :keyword || '%' ORDER BY timestamp DESC")
    fun searchStatesByCustomData(keyword: String): Flow<List<RitsuState>>
    
    @Query("SELECT DISTINCT current_animation FROM ritsu_states WHERE current_animation IS NOT NULL")
    suspend fun getAvailableAnimations(): List<String>
    
    @Query("SELECT DISTINCT current_context FROM ritsu_states WHERE current_context IS NOT NULL")
    suspend fun getAvailableContexts(): List<String>
}