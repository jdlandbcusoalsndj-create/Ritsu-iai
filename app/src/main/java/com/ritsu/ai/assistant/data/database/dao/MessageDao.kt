package com.ritsu.ai.assistant.data.database.dao

import androidx.room.*
import com.ritsu.ai.assistant.data.database.MessageType
import com.ritsu.ai.assistant.data.database.entity.Message
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface MessageDao {
    
    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<Message>>
    
    @Query("SELECT * FROM messages WHERE id = :id")
    suspend fun getMessageById(id: Long): Message?
    
    @Query("SELECT * FROM messages WHERE contact_id = :contactId ORDER BY timestamp DESC")
    fun getMessagesByContact(contactId: Long): Flow<List<Message>>
    
    @Query("SELECT * FROM messages WHERE type = :type ORDER BY timestamp DESC")
    fun getMessagesByType(type: MessageType): Flow<List<Message>>
    
    @Query("SELECT * FROM messages WHERE is_incoming = :isIncoming ORDER BY timestamp DESC")
    fun getMessagesByDirection(isIncoming: Boolean): Flow<List<Message>>
    
    @Query("SELECT * FROM messages WHERE is_read = :isRead ORDER BY timestamp DESC")
    fun getMessagesByReadStatus(isRead: Boolean): Flow<List<Message>>
    
    @Query("SELECT * FROM messages WHERE timestamp >= :startDate ORDER BY timestamp DESC")
    fun getMessagesFromDate(startDate: LocalDateTime): Flow<List<Message>>
    
    @Query("SELECT * FROM messages WHERE language = :language ORDER BY timestamp DESC")
    fun getMessagesByLanguage(language: String): Flow<List<Message>>
    
    @Query("SELECT * FROM messages WHERE is_processed_by_ritsu = :processed ORDER BY timestamp DESC")
    fun getMessagesByProcessedStatus(processed: Boolean): Flow<List<Message>>
    
    @Query("SELECT * FROM messages ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentMessages(limit: Int): List<Message>
    
    @Query("SELECT COUNT(*) FROM messages WHERE is_read = 0")
    suspend fun getUnreadMessageCount(): Int
    
    @Query("SELECT COUNT(*) FROM messages WHERE timestamp >= :startDate")
    suspend fun getMessageCountFromDate(startDate: LocalDateTime): Int
    
    @Query("SELECT AVG(sentiment_score) FROM messages WHERE sentiment_score IS NOT NULL")
    suspend fun getAverageSentimentScore(): Float?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<Message>)
    
    @Update
    suspend fun updateMessage(message: Message)
    
    @Delete
    suspend fun deleteMessage(message: Message)
    
    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteMessageById(id: Long)
    
    @Query("DELETE FROM messages WHERE timestamp < :date")
    suspend fun deleteMessagesOlderThan(date: LocalDateTime)
    
    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()
    
    @Query("UPDATE messages SET is_read = :isRead WHERE id = :id")
    suspend fun updateReadStatus(id: Long, isRead: Boolean)
    
    @Query("UPDATE messages SET is_processed_by_ritsu = :processed WHERE id = :id")
    suspend fun updateProcessedStatus(id: Long, processed: Boolean)
    
    @Query("UPDATE messages SET ritsu_response = :response WHERE id = :id")
    suspend fun updateRitsuResponse(id: Long, response: String)
    
    @Query("UPDATE messages SET sentiment_score = :score WHERE id = :id")
    suspend fun updateSentimentScore(id: Long, score: Float)
    
    @Query("UPDATE messages SET priority = :priority WHERE id = :id")
    suspend fun updatePriority(id: Long, priority: Int)
    
    @Query("SELECT DISTINCT language FROM messages")
    suspend fun getAvailableLanguages(): List<String>
    
    @Query("SELECT * FROM messages WHERE content LIKE '%' || :keyword || '%' ORDER BY timestamp DESC")
    fun searchMessages(keyword: String): Flow<List<Message>>
    
    @Query("SELECT * FROM messages WHERE thread_id = :threadId ORDER BY timestamp ASC")
    fun getMessagesByThread(threadId: String): Flow<List<Message>>
    
    @Query("SELECT DISTINCT thread_id FROM messages WHERE thread_id IS NOT NULL")
    suspend fun getAvailableThreads(): List<String>
}