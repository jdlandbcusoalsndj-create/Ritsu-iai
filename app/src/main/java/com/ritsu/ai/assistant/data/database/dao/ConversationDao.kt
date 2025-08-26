package com.ritsu.ai.assistant.data.database.dao

import androidx.room.*
import com.ritsu.ai.assistant.data.database.ConversationType
import com.ritsu.ai.assistant.data.database.entity.Conversation
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface ConversationDao {
    
    @Query("SELECT * FROM conversations ORDER BY timestamp DESC")
    fun getAllConversations(): Flow<List<Conversation>>
    
    @Query("SELECT * FROM conversations WHERE contact_id = :contactId ORDER BY timestamp DESC")
    fun getConversationsByContact(contactId: Long): Flow<List<Conversation>>
    
    @Query("SELECT * FROM conversations WHERE type = :type ORDER BY timestamp DESC")
    fun getConversationsByType(type: ConversationType): Flow<List<Conversation>>
    
    @Query("SELECT * FROM conversations WHERE timestamp >= :startDate ORDER BY timestamp DESC")
    fun getConversationsFromDate(startDate: LocalDateTime): Flow<List<Conversation>>
    
    @Query("SELECT * FROM conversations WHERE language = :language ORDER BY timestamp DESC")
    fun getConversationsByLanguage(language: String): Flow<List<Conversation>>
    
    @Query("SELECT * FROM conversations WHERE is_processed = :processed ORDER BY timestamp DESC")
    fun getConversationsByProcessedStatus(processed: Boolean): Flow<List<Conversation>>
    
    @Query("SELECT * FROM conversations WHERE id = :id")
    suspend fun getConversationById(id: Long): Conversation?
    
    @Query("SELECT * FROM conversations ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentConversations(limit: Int): List<Conversation>
    
    @Query("SELECT COUNT(*) FROM conversations WHERE timestamp >= :startDate")
    suspend fun getConversationCountFromDate(startDate: LocalDateTime): Int
    
    @Query("SELECT AVG(sentiment_score) FROM conversations WHERE sentiment_score IS NOT NULL")
    suspend fun getAverageSentimentScore(): Float?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: Conversation): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(conversations: List<Conversation>)
    
    @Update
    suspend fun updateConversation(conversation: Conversation)
    
    @Delete
    suspend fun deleteConversation(conversation: Conversation)
    
    @Query("DELETE FROM conversations WHERE id = :id")
    suspend fun deleteConversationById(id: Long)
    
    @Query("DELETE FROM conversations WHERE timestamp < :date")
    suspend fun deleteConversationsOlderThan(date: LocalDateTime)
    
    @Query("DELETE FROM conversations")
    suspend fun deleteAllConversations()
    
    @Query("UPDATE conversations SET is_processed = :processed WHERE id = :id")
    suspend fun updateProcessedStatus(id: Long, processed: Boolean)
    
    @Query("UPDATE conversations SET sentiment_score = :score WHERE id = :id")
    suspend fun updateSentimentScore(id: Long, score: Float)
    
    @Query("SELECT DISTINCT language FROM conversations")
    suspend fun getAvailableLanguages(): List<String>
    
    @Query("SELECT * FROM conversations WHERE content LIKE '%' || :keyword || '%' ORDER BY timestamp DESC")
    fun searchConversations(keyword: String): Flow<List<Conversation>>
}