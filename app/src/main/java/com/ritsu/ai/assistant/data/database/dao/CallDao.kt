package com.ritsu.ai.assistant.data.database.dao

import androidx.room.*
import com.ritsu.ai.assistant.data.database.CallType
import com.ritsu.ai.assistant.data.database.entity.Call
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface CallDao {
    
    @Query("SELECT * FROM calls ORDER BY timestamp DESC")
    fun getAllCalls(): Flow<List<Call>>
    
    @Query("SELECT * FROM calls WHERE id = :id")
    suspend fun getCallById(id: Long): Call?
    
    @Query("SELECT * FROM calls WHERE contact_id = :contactId ORDER BY timestamp DESC")
    fun getCallsByContact(contactId: Long): Flow<List<Call>>
    
    @Query("SELECT * FROM calls WHERE type = :type ORDER BY timestamp DESC")
    fun getCallsByType(type: CallType): Flow<List<Call>>
    
    @Query("SELECT * FROM calls WHERE phone_number = :phoneNumber ORDER BY timestamp DESC")
    fun getCallsByPhoneNumber(phoneNumber: String): Flow<List<Call>>
    
    @Query("SELECT * FROM calls WHERE was_answered_by_ritsu = :answeredByRitsu ORDER BY timestamp DESC")
    fun getCallsByRitsuAnswer(answeredByRitsu: Boolean): Flow<List<Call>>
    
    @Query("SELECT * FROM calls WHERE timestamp >= :startDate ORDER BY timestamp DESC")
    fun getCallsFromDate(startDate: LocalDateTime): Flow<List<Call>>
    
    @Query("SELECT * FROM calls WHERE is_spam = :isSpam ORDER BY timestamp DESC")
    fun getCallsBySpamStatus(isSpam: Boolean): Flow<List<Call>>
    
    @Query("SELECT * FROM calls ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentCalls(limit: Int): List<Call>
    
    @Query("SELECT COUNT(*) FROM calls WHERE timestamp >= :startDate")
    suspend fun getCallCountFromDate(startDate: LocalDateTime): Int
    
    @Query("SELECT COUNT(*) FROM calls WHERE was_answered_by_ritsu = 1")
    suspend fun getCallsAnsweredByRitsuCount(): Int
    
    @Query("SELECT AVG(duration_seconds) FROM calls WHERE duration_seconds > 0")
    suspend fun getAverageCallDuration(): Float?
    
    @Query("SELECT AVG(sentiment_score) FROM calls WHERE sentiment_score IS NOT NULL")
    suspend fun getAverageSentimentScore(): Float?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCall(call: Call): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalls(calls: List<Call>)
    
    @Update
    suspend fun updateCall(call: Call)
    
    @Delete
    suspend fun deleteCall(call: Call)
    
    @Query("DELETE FROM calls WHERE id = :id")
    suspend fun deleteCallById(id: Long)
    
    @Query("DELETE FROM calls WHERE timestamp < :date")
    suspend fun deleteCallsOlderThan(date: LocalDateTime)
    
    @Query("DELETE FROM calls")
    suspend fun deleteAllCalls()
    
    @Query("UPDATE calls SET duration_seconds = :duration WHERE id = :id")
    suspend fun updateCallDuration(id: Long, duration: Int)
    
    @Query("UPDATE calls SET was_answered_by_ritsu = :answered WHERE id = :id")
    suspend fun updateCallAnsweredByRitsu(id: Long, answered: Boolean)
    
    @Query("UPDATE calls SET ritsu_conversation_summary = :summary WHERE id = :id")
    suspend fun updateCallSummary(id: Long, summary: String)
    
    @Query("UPDATE calls SET transcription = :transcription WHERE id = :id")
    suspend fun updateCallTranscription(id: Long, transcription: String)
    
    @Query("UPDATE calls SET sentiment_score = :score WHERE id = :id")
    suspend fun updateSentimentScore(id: Long, score: Float)
    
    @Query("UPDATE calls SET is_spam = :isSpam WHERE id = :id")
    suspend fun updateSpamStatus(id: Long, isSpam: Boolean)
    
    @Query("UPDATE calls SET spam_confidence = :confidence WHERE id = :id")
    suspend fun updateSpamConfidence(id: Long, confidence: Float)
    
    @Query("UPDATE calls SET notes = :notes WHERE id = :id")
    suspend fun updateCallNotes(id: Long, notes: String)
    
    @Query("UPDATE calls SET recording_path = :path WHERE id = :id")
    suspend fun updateRecordingPath(id: Long, path: String)
    
    @Query("UPDATE calls SET type = :type WHERE id = :id")
    suspend fun updateCallType(id: Long, type: CallType)
    
    @Query("SELECT * FROM calls WHERE content LIKE '%' || :keyword || '%' ORDER BY timestamp DESC")
    fun searchCalls(keyword: String): Flow<List<Call>>
    
    @Query("SELECT DISTINCT phone_number FROM calls WHERE phone_number IS NOT NULL")
    suspend fun getAvailablePhoneNumbers(): List<String>
    
    @Query("SELECT * FROM calls WHERE duration_seconds > :minDuration ORDER BY timestamp DESC")
    fun getCallsByMinDuration(minDuration: Int): Flow<List<Call>>
    
    @Query("SELECT * FROM calls WHERE sentiment_score < :maxSentiment ORDER BY timestamp DESC")
    fun getCallsByMaxSentiment(maxSentiment: Float): Flow<List<Call>>
}