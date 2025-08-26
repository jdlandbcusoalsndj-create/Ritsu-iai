package com.ritsu.ai.assistant.data.repository

import com.ritsu.ai.assistant.data.database.MessageType
import com.ritsu.ai.assistant.data.database.dao.MessageDao
import com.ritsu.ai.assistant.data.database.entity.Message
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class MessageRepository(private val messageDao: MessageDao) {
    
    fun getAllMessages(): Flow<List<Message>> = messageDao.getAllMessages()
    
    suspend fun getMessageById(id: Long): Message? = messageDao.getMessageById(id)
    
    fun getMessagesByContact(contactId: Long): Flow<List<Message>> = 
        messageDao.getMessagesByContact(contactId)
    
    fun getMessagesByType(type: MessageType): Flow<List<Message>> = 
        messageDao.getMessagesByType(type)
    
    fun getMessagesByDirection(isIncoming: Boolean): Flow<List<Message>> = 
        messageDao.getMessagesByDirection(isIncoming)
    
    fun getMessagesByReadStatus(isRead: Boolean): Flow<List<Message>> = 
        messageDao.getMessagesByReadStatus(isRead)
    
    fun getMessagesFromDate(startDate: LocalDateTime): Flow<List<Message>> = 
        messageDao.getMessagesFromDate(startDate)
    
    fun getMessagesByLanguage(language: String): Flow<List<Message>> = 
        messageDao.getMessagesByLanguage(language)
    
    fun getMessagesByProcessedStatus(processed: Boolean): Flow<List<Message>> = 
        messageDao.getMessagesByProcessedStatus(processed)
    
    suspend fun getRecentMessages(limit: Int): List<Message> = messageDao.getRecentMessages(limit)
    
    suspend fun getUnreadMessageCount(): Int = messageDao.getUnreadMessageCount()
    
    suspend fun getMessageCountFromDate(startDate: LocalDateTime): Int = 
        messageDao.getMessageCountFromDate(startDate)
    
    suspend fun getAverageSentimentScore(): Float? = messageDao.getAverageSentimentScore()
    
    suspend fun insertMessage(message: Message): Long = messageDao.insertMessage(message)
    
    suspend fun insertMessages(messages: List<Message>) = messageDao.insertMessages(messages)
    
    suspend fun updateMessage(message: Message) = messageDao.updateMessage(message)
    
    suspend fun deleteMessage(message: Message) = messageDao.deleteMessage(message)
    
    suspend fun deleteMessageById(id: Long) = messageDao.deleteMessageById(id)
    
    suspend fun deleteMessagesOlderThan(date: LocalDateTime) = 
        messageDao.deleteMessagesOlderThan(date)
    
    suspend fun deleteAllMessages() = messageDao.deleteAllMessages()
    
    suspend fun updateReadStatus(id: Long, isRead: Boolean) = 
        messageDao.updateReadStatus(id, isRead)
    
    suspend fun updateProcessedStatus(id: Long, processed: Boolean) = 
        messageDao.updateProcessedStatus(id, processed)
    
    suspend fun updateRitsuResponse(id: Long, response: String) = 
        messageDao.updateRitsuResponse(id, response)
    
    suspend fun updateSentimentScore(id: Long, score: Float) = 
        messageDao.updateSentimentScore(id, score)
    
    suspend fun updatePriority(id: Long, priority: Int) = messageDao.updatePriority(id, priority)
    
    suspend fun getAvailableLanguages(): List<String> = messageDao.getAvailableLanguages()
    
    fun searchMessages(keyword: String): Flow<List<Message>> = messageDao.searchMessages(keyword)
    
    fun getMessagesByThread(threadId: String): Flow<List<Message>> = 
        messageDao.getMessagesByThread(threadId)
    
    suspend fun getAvailableThreads(): List<String> = messageDao.getAvailableThreads()
}