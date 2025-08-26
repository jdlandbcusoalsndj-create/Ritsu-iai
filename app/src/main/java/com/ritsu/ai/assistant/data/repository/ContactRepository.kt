package com.ritsu.ai.assistant.data.repository

import com.ritsu.ai.assistant.data.database.dao.ContactDao
import com.ritsu.ai.assistant.data.database.entity.Contact
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class ContactRepository(private val contactDao: ContactDao) {
    
    fun getAllContacts(): Flow<List<Contact>> = contactDao.getAllContacts()
    
    suspend fun getContactById(id: Long): Contact? = contactDao.getContactById(id)
    
    suspend fun getContactByPhoneNumber(phoneNumber: String): Contact? = 
        contactDao.getContactByPhoneNumber(phoneNumber)
    
    suspend fun getContactByEmail(email: String): Contact? = contactDao.getContactByEmail(email)
    
    fun getFavoriteContacts(): Flow<List<Contact>> = contactDao.getFavoriteContacts()
    
    fun getContactsWithAutoAnswer(): Flow<List<Contact>> = contactDao.getContactsWithAutoAnswer()
    
    fun getContactsByRelationship(relationship: String): Flow<List<Contact>> = 
        contactDao.getContactsByRelationship(relationship)
    
    fun getContactsByLanguage(language: String): Flow<List<Contact>> = 
        contactDao.getContactsByLanguage(language)
    
    fun getContactsWithRecentInteraction(date: LocalDateTime): Flow<List<Contact>> = 
        contactDao.getContactsWithRecentInteraction(date)
    
    suspend fun getMostInteractedContacts(limit: Int): List<Contact> = 
        contactDao.getMostInteractedContacts(limit)
    
    suspend fun getContactCount(): Int = contactDao.getContactCount()
    
    suspend fun getFavoriteContactCount(): Int = contactDao.getFavoriteContactCount()
    
    suspend fun insertContact(contact: Contact): Long = contactDao.insertContact(contact)
    
    suspend fun insertContacts(contacts: List<Contact>) = contactDao.insertContacts(contacts)
    
    suspend fun updateContact(contact: Contact) = contactDao.updateContact(contact)
    
    suspend fun deleteContact(contact: Contact) = contactDao.deleteContact(contact)
    
    suspend fun deleteContactById(id: Long) = contactDao.deleteContactById(id)
    
    suspend fun deleteAllContacts() = contactDao.deleteAllContacts()
    
    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean) = 
        contactDao.updateFavoriteStatus(id, isFavorite)
    
    suspend fun updateAutoAnswerStatus(id: Long, enabled: Boolean) = 
        contactDao.updateAutoAnswerStatus(id, enabled)
    
    suspend fun updateInteractionData(id: Long, date: LocalDateTime) = 
        contactDao.updateInteractionData(id, date)
    
    suspend fun updateRitsuPersonality(id: Long, personality: String) = 
        contactDao.updateRitsuPersonality(id, personality)
    
    suspend fun updateCustomGreeting(id: Long, greeting: String) = 
        contactDao.updateCustomGreeting(id, greeting)
    
    suspend fun updateCustomFarewell(id: Long, farewell: String) = 
        contactDao.updateCustomFarewell(id, farewell)
    
    suspend fun updateImportantNotes(id: Long, notes: String) = 
        contactDao.updateImportantNotes(id, notes)
    
    fun searchContacts(keyword: String): Flow<List<Contact>> = contactDao.searchContacts(keyword)
    
    suspend fun getAvailableRelationships(): List<String> = contactDao.getAvailableRelationships()
    
    suspend fun getAvailableLanguages(): List<String> = contactDao.getAvailableLanguages()
}