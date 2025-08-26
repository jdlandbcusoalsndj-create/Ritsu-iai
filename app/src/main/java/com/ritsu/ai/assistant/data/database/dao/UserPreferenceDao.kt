package com.ritsu.ai.assistant.data.database.dao

import androidx.room.*
import com.ritsu.ai.assistant.data.database.entity.UserPreference
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface UserPreferenceDao {
    
    @Query("SELECT * FROM user_preferences ORDER BY category, preference_key")
    fun getAllPreferences(): Flow<List<UserPreference>>
    
    @Query("SELECT * FROM user_preferences WHERE id = :id")
    suspend fun getPreferenceById(id: Long): UserPreference?
    
    @Query("SELECT * FROM user_preferences WHERE preference_key = :key")
    suspend fun getPreferenceByKey(key: String): UserPreference?
    
    @Query("SELECT * FROM user_preferences WHERE category = :category ORDER BY preference_key")
    fun getPreferencesByCategory(category: String): Flow<List<UserPreference>>
    
    @Query("SELECT * FROM user_preferences WHERE preference_type = :type ORDER BY preference_key")
    fun getPreferencesByType(type: String): Flow<List<UserPreference>>
    
    @Query("SELECT * FROM user_preferences WHERE is_system = :isSystem ORDER BY preference_key")
    fun getPreferencesBySystemStatus(isSystem: Boolean): Flow<List<UserPreference>>
    
    @Query("SELECT preference_value FROM user_preferences WHERE preference_key = :key")
    suspend fun getPreferenceValue(key: String): String?
    
    @Query("SELECT COUNT(*) FROM user_preferences")
    suspend fun getPreferenceCount(): Int
    
    @Query("SELECT COUNT(*) FROM user_preferences WHERE category = :category")
    suspend fun getPreferenceCountByCategory(category: String): Int
    
    @Query("SELECT DISTINCT category FROM user_preferences ORDER BY category")
    suspend fun getAvailableCategories(): List<String>
    
    @Query("SELECT DISTINCT preference_type FROM user_preferences ORDER BY preference_type")
    suspend fun getAvailableTypes(): List<String>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreference(preference: UserPreference): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreferences(preferences: List<UserPreference>)
    
    @Update
    suspend fun updatePreference(preference: UserPreference)
    
    @Delete
    suspend fun deletePreference(preference: UserPreference)
    
    @Query("DELETE FROM user_preferences WHERE id = :id")
    suspend fun deletePreferenceById(id: Long)
    
    @Query("DELETE FROM user_preferences WHERE preference_key = :key")
    suspend fun deletePreferenceByKey(key: String)
    
    @Query("DELETE FROM user_preferences WHERE category = :category")
    suspend fun deletePreferencesByCategory(category: String)
    
    @Query("DELETE FROM user_preferences")
    suspend fun deleteAllPreferences()
    
    @Query("UPDATE user_preferences SET preference_value = :value WHERE preference_key = :key")
    suspend fun updatePreferenceValue(key: String, value: String)
    
    @Query("UPDATE user_preferences SET preference_type = :type WHERE preference_key = :key")
    suspend fun updatePreferenceType(key: String, type: String)
    
    @Query("UPDATE user_preferences SET category = :category WHERE preference_key = :key")
    suspend fun updatePreferenceCategory(key: String, category: String)
    
    @Query("UPDATE user_preferences SET description = :description WHERE preference_key = :key")
    suspend fun updatePreferenceDescription(key: String, description: String)
    
    @Query("UPDATE user_preferences SET is_system = :isSystem WHERE preference_key = :key")
    suspend fun updatePreferenceSystemStatus(key: String, isSystem: Boolean)
    
    @Query("UPDATE user_preferences SET updated_at = :date WHERE preference_key = :key")
    suspend fun updatePreferenceTimestamp(key: String, date: LocalDateTime)
    
    @Query("SELECT * FROM user_preferences WHERE preference_key LIKE '%' || :keyword || '%' OR description LIKE '%' || :keyword || '%' ORDER BY preference_key")
    fun searchPreferences(keyword: String): Flow<List<UserPreference>>
    
    // Métodos de conveniencia para tipos específicos
    @Query("SELECT preference_value FROM user_preferences WHERE preference_key = :key AND preference_type = 'boolean'")
    suspend fun getBooleanPreference(key: String): Boolean?
    
    @Query("SELECT preference_value FROM user_preferences WHERE preference_key = :key AND preference_type = 'int'")
    suspend fun getIntPreference(key: String): Int?
    
    @Query("SELECT preference_value FROM user_preferences WHERE preference_key = :key AND preference_type = 'float'")
    suspend fun getFloatPreference(key: String): Float?
    
    @Query("SELECT preference_value FROM user_preferences WHERE preference_key = :key AND preference_type = 'string'")
    suspend fun getStringPreference(key: String): String?
    
    // Métodos para actualizar tipos específicos
    @Query("UPDATE user_preferences SET preference_value = :value WHERE preference_key = :key AND preference_type = 'boolean'")
    suspend fun updateBooleanPreference(key: String, value: Boolean)
    
    @Query("UPDATE user_preferences SET preference_value = :value WHERE preference_key = :key AND preference_type = 'int'")
    suspend fun updateIntPreference(key: String, value: Int)
    
    @Query("UPDATE user_preferences SET preference_value = :value WHERE preference_key = :key AND preference_type = 'float'")
    suspend fun updateFloatPreference(key: String, value: Float)
    
    @Query("UPDATE user_preferences SET preference_value = :value WHERE preference_key = :key AND preference_type = 'string'")
    suspend fun updateStringPreference(key: String, value: String)
}