package com.ritsu.ai.assistant.data.database.dao

import androidx.room.*
import com.ritsu.ai.assistant.data.database.entity.Contact
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface ContactDao {
    
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContacts(): Flow<List<Contact>>
    
    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: Long): Contact?
    
    @Query("SELECT * FROM contacts WHERE phone_number = :phoneNumber")
    suspend fun getContactByPhoneNumber(phoneNumber: String): Contact?
    
    @Query("SELECT * FROM contacts WHERE email = :email")
    suspend fun getContactByEmail(email: String): Contact?
    
    @Query("SELECT * FROM contacts WHERE is_favorite = 1 ORDER BY name ASC")
    fun getFavoriteContacts(): Flow<List<Contact>>
    
    @Query("SELECT * FROM contacts WHERE auto_answer_enabled = 1 ORDER BY name ASC")
    fun getContactsWithAutoAnswer(): Flow<List<Contact>>
    
    @Query("SELECT * FROM contacts WHERE relationship = :relationship ORDER BY name ASC")
    fun getContactsByRelationship(relationship: String): Flow<List<Contact>>
    
    @Query("SELECT * FROM contacts WHERE preferred_language = :language ORDER BY name ASC")
    fun getContactsByLanguage(language: String): Flow<List<Contact>>
    
    @Query("SELECT * FROM contacts WHERE last_interaction >= :date ORDER BY last_interaction DESC")
    fun getContactsWithRecentInteraction(date: LocalDateTime): Flow<List<Contact>>
    
    @Query("SELECT * FROM contacts ORDER BY interaction_count DESC LIMIT :limit")
    suspend fun getMostInteractedContacts(limit: Int): List<Contact>
    
    @Query("SELECT COUNT(*) FROM contacts")
    suspend fun getContactCount(): Int
    
    @Query("SELECT COUNT(*) FROM contacts WHERE is_favorite = 1")
    suspend fun getFavoriteContactCount(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contacts: List<Contact>)
    
    @Update
    suspend fun updateContact(contact: Contact)
    
    @Delete
    suspend fun deleteContact(contact: Contact)
    
    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun deleteContactById(id: Long)
    
    @Query("DELETE FROM contacts")
    suspend fun deleteAllContacts()
    
    @Query("UPDATE contacts SET is_favorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean)
    
    @Query("UPDATE contacts SET auto_answer_enabled = :enabled WHERE id = :id")
    suspend fun updateAutoAnswerStatus(id: Long, enabled: Boolean)
    
    @Query("UPDATE contacts SET last_interaction = :date, interaction_count = interaction_count + 1 WHERE id = :id")
    suspend fun updateInteractionData(id: Long, date: LocalDateTime)
    
    @Query("UPDATE contacts SET ritsu_personality = :personality WHERE id = :id")
    suspend fun updateRitsuPersonality(id: Long, personality: String)
    
    @Query("UPDATE contacts SET custom_greeting = :greeting WHERE id = :id")
    suspend fun updateCustomGreeting(id: Long, greeting: String)
    
    @Query("UPDATE contacts SET custom_farewell = :farewell WHERE id = :id")
    suspend fun updateCustomFarewell(id: Long, farewell: String)
    
    @Query("UPDATE contacts SET important_notes = :notes WHERE id = :id")
    suspend fun updateImportantNotes(id: Long, notes: String)
    
    @Query("SELECT * FROM contacts WHERE name LIKE '%' || :keyword || '%' OR phone_number LIKE '%' || :keyword || '%' ORDER BY name ASC")
    fun searchContacts(keyword: String): Flow<List<Contact>>
    
    @Query("SELECT DISTINCT relationship FROM contacts WHERE relationship IS NOT NULL")
    suspend fun getAvailableRelationships(): List<String>
    
    @Query("SELECT DISTINCT preferred_language FROM contacts")
    suspend fun getAvailableLanguages(): List<String>
}