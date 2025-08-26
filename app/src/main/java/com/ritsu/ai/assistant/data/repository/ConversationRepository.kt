package com.ritsu.ai.assistant.data.repository

import com.ritsu.ai.assistant.data.database.ConversationType
import com.ritsu.ai.assistant.data.database.dao.ConversationDao
import com.ritsu.ai.assistant.data.database.entity.Conversation
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class ConversationRepository(private val conversationDao: ConversationDao) {
    
    fun getAllConversations(): Flow<List<Conversation>> = conversationDao.getAllConversations()
    
    fun getConversationsByContact(contactId: Long): Flow<List<Conversation>> = 
        conversationDao.getConversationsByContact(contactId)
    
    fun getConversationsByType(type: ConversationType): Flow<List<Conversation>> = 
        conversationDao.getConversationsByType(type)
    
    fun getConversationsFromDate(startDate: LocalDateTime): Flow<List<Conversation>> = 
        conversationDao.getConversationsFromDate(startDate)
    
    fun getConversationsByLanguage(language: String): Flow<List<Conversation>> = 
        conversationDao.getConversationsByLanguage(language)
    
    fun getConversationsByProcessedStatus(processed: Boolean): Flow<List<Conversation>> = 
        conversationDao.getConversationsByProcessedStatus(processed)
    
    suspend fun getConversationById(id: Long): Conversation? = conversationDao.getConversationById(id)
    
    suspend fun getRecentConversations(limit: Int): List<Conversation> = 
        conversationDao.getRecentConversations(limit)
    
    suspend fun getConversationCountFromDate(startDate: LocalDateTime): Int = 
        conversationDao.getConversationCountFromDate(startDate)
    
    suspend fun getAverageSentimentScore(): Float? = conversationDao.getAverageSentimentScore()
    
    suspend fun insertConversation(conversation: Conversation): Long = 
        conversationDao.insertConversation(conversation)
    
    suspend fun insertConversations(conversations: List<Conversation>) = 
        conversationDao.insertConversations(conversations)
    
    suspend fun updateConversation(conversation: Conversation) = 
        conversationDao.updateConversation(conversation)
    
    suspend fun deleteConversation(conversation: Conversation) = 
        conversationDao.deleteConversation(conversation)
    
    suspend fun deleteConversationById(id: Long) = conversationDao.deleteConversationById(id)
    
    suspend fun deleteConversationsOlderThan(date: LocalDateTime) = 
        conversationDao.deleteConversationsOlderThan(date)
    
    suspend fun deleteAllConversations() = conversationDao.deleteAllConversations()
    
    suspend fun updateProcessedStatus(id: Long, processed: Boolean) = 
        conversationDao.updateProcessedStatus(id, processed)
    
    suspend fun updateSentimentScore(id: Long, score: Float) = 
        conversationDao.updateSentimentScore(id, score)
    
    suspend fun getAvailableLanguages(): List<String> = conversationDao.getAvailableLanguages()
    
    fun searchConversations(keyword: String): Flow<List<Conversation>> = 
        conversationDao.searchConversations(keyword)
}