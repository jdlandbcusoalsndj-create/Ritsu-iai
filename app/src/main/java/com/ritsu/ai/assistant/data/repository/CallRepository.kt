package com.ritsu.ai.assistant.data.repository

import com.ritsu.ai.assistant.data.database.CallType
import com.ritsu.ai.assistant.data.database.dao.CallDao
import com.ritsu.ai.assistant.data.database.entity.Call
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class CallRepository(private val callDao: CallDao) {
    
    fun getAllCalls(): Flow<List<Call>> = callDao.getAllCalls()
    
    suspend fun getCallById(id: Long): Call? = callDao.getCallById(id)
    
    fun getCallsByContact(contactId: Long): Flow<List<Call>> = callDao.getCallsByContact(contactId)
    
    fun getCallsByType(type: CallType): Flow<List<Call>> = callDao.getCallsByType(type)
    
    fun getCallsByPhoneNumber(phoneNumber: String): Flow<List<Call>> = 
        callDao.getCallsByPhoneNumber(phoneNumber)
    
    fun getCallsByRitsuAnswer(answeredByRitsu: Boolean): Flow<List<Call>> = 
        callDao.getCallsByRitsuAnswer(answeredByRitsu)
    
    fun getCallsFromDate(startDate: LocalDateTime): Flow<List<Call>> = 
        callDao.getCallsFromDate(startDate)
    
    fun getCallsBySpamStatus(isSpam: Boolean): Flow<List<Call>> = 
        callDao.getCallsBySpamStatus(isSpam)
    
    suspend fun getRecentCalls(limit: Int): List<Call> = callDao.getRecentCalls(limit)
    
    suspend fun getCallCountFromDate(startDate: LocalDateTime): Int = 
        callDao.getCallCountFromDate(startDate)
    
    suspend fun getCallsAnsweredByRitsuCount(): Int = callDao.getCallsAnsweredByRitsuCount()
    
    suspend fun getAverageCallDuration(): Float? = callDao.getAverageCallDuration()
    
    suspend fun getAverageSentimentScore(): Float? = callDao.getAverageSentimentScore()
    
    suspend fun insertCall(call: Call): Long = callDao.insertCall(call)
    
    suspend fun insertCalls(calls: List<Call>) = callDao.insertCalls(calls)
    
    suspend fun updateCall(call: Call) = callDao.updateCall(call)
    
    suspend fun deleteCall(call: Call) = callDao.deleteCall(call)
    
    suspend fun deleteCallById(id: Long) = callDao.deleteCallById(id)
    
    suspend fun deleteCallsOlderThan(date: LocalDateTime) = callDao.deleteCallsOlderThan(date)
    
    suspend fun deleteAllCalls() = callDao.deleteAllCalls()
    
    suspend fun updateCallDuration(id: Long, duration: Int) = callDao.updateCallDuration(id, duration)
    
    suspend fun updateCallAnsweredByRitsu(id: Long, answered: Boolean) = 
        callDao.updateCallAnsweredByRitsu(id, answered)
    
    suspend fun updateCallSummary(id: Long, summary: String) = callDao.updateCallSummary(id, summary)
    
    suspend fun updateCallTranscription(id: Long, transcription: String) = 
        callDao.updateCallTranscription(id, transcription)
    
    suspend fun updateSentimentScore(id: Long, score: Float) = callDao.updateSentimentScore(id, score)
    
    suspend fun updateSpamStatus(id: Long, isSpam: Boolean) = callDao.updateSpamStatus(id, isSpam)
    
    suspend fun updateSpamConfidence(id: Long, confidence: Float) = 
        callDao.updateSpamConfidence(id, confidence)
    
    suspend fun updateCallNotes(id: Long, notes: String) = callDao.updateCallNotes(id, notes)
    
    suspend fun updateRecordingPath(id: Long, path: String) = callDao.updateRecordingPath(id, path)
    
    suspend fun updateCallType(id: Long, type: CallType) = callDao.updateCallType(id, type)
    
    fun searchCalls(keyword: String): Flow<List<Call>> = callDao.searchCalls(keyword)
    
    suspend fun getAvailablePhoneNumbers(): List<String> = callDao.getAvailablePhoneNumbers()
    
    fun getCallsByMinDuration(minDuration: Int): Flow<List<Call>> = 
        callDao.getCallsByMinDuration(minDuration)
    
    fun getCallsByMaxSentiment(maxSentiment: Float): Flow<List<Call>> = 
        callDao.getCallsByMaxSentiment(maxSentiment)
}