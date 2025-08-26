package com.ritsu.ai.assistant.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val Migration1To2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Agregar nuevas columnas a la tabla conversations
        database.execSQL("ALTER TABLE conversations ADD COLUMN sentiment_score REAL")
        database.execSQL("ALTER TABLE conversations ADD COLUMN context_data TEXT")
        database.execSQL("ALTER TABLE conversations ADD COLUMN is_processed INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE conversations ADD COLUMN tags TEXT")
        
        // Agregar nuevas columnas a la tabla contacts
        database.execSQL("ALTER TABLE contacts ADD COLUMN ritsu_personality TEXT NOT NULL DEFAULT 'friendly'")
        database.execSQL("ALTER TABLE contacts ADD COLUMN auto_answer_enabled INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE contacts ADD COLUMN custom_greeting TEXT")
        database.execSQL("ALTER TABLE contacts ADD COLUMN custom_farewell TEXT")
        database.execSQL("ALTER TABLE contacts ADD COLUMN important_notes TEXT")
        database.execSQL("ALTER TABLE contacts ADD COLUMN preferred_language TEXT NOT NULL DEFAULT 'es'")
        database.execSQL("ALTER TABLE contacts ADD COLUMN last_interaction INTEGER")
        database.execSQL("ALTER TABLE contacts ADD COLUMN interaction_count INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE contacts ADD COLUMN is_favorite INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE contacts ADD COLUMN tags TEXT")
        
        // Agregar nuevas columnas a la tabla messages
        database.execSQL("ALTER TABLE messages ADD COLUMN is_processed_by_ritsu INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE messages ADD COLUMN ritsu_response TEXT")
        database.execSQL("ALTER TABLE messages ADD COLUMN language TEXT NOT NULL DEFAULT 'es'")
        database.execSQL("ALTER TABLE messages ADD COLUMN sentiment_score REAL")
        database.execSQL("ALTER TABLE messages ADD COLUMN priority INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE messages ADD COLUMN attachments TEXT")
        database.execSQL("ALTER TABLE messages ADD COLUMN thread_id TEXT")
        database.execSQL("ALTER TABLE messages ADD COLUMN external_id TEXT")
        database.execSQL("ALTER TABLE messages ADD COLUMN tags TEXT")
        
        // Agregar nuevas columnas a la tabla calls
        database.execSQL("ALTER TABLE calls ADD COLUMN was_answered_by_ritsu INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE calls ADD COLUMN ritsu_conversation_summary TEXT")
        database.execSQL("ALTER TABLE calls ADD COLUMN transcription TEXT")
        database.execSQL("ALTER TABLE calls ADD COLUMN sentiment_score REAL")
        database.execSQL("ALTER TABLE calls ADD COLUMN priority INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE calls ADD COLUMN is_spam INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE calls ADD COLUMN spam_confidence REAL")
        database.execSQL("ALTER TABLE calls ADD COLUMN notes TEXT")
        database.execSQL("ALTER TABLE calls ADD COLUMN recording_path TEXT")
        database.execSQL("ALTER TABLE calls ADD COLUMN external_id TEXT")
        database.execSQL("ALTER TABLE calls ADD COLUMN tags TEXT")
        
        // Crear índices para mejorar el rendimiento
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_conversations_sentiment ON conversations(sentiment_score)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_conversations_processed ON conversations(is_processed)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_contacts_personality ON contacts(ritsu_personality)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_contacts_auto_answer ON contacts(auto_answer_enabled)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_contacts_favorite ON contacts(is_favorite)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_messages_processed ON messages(is_processed_by_ritsu)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_messages_priority ON messages(priority)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_calls_answered ON calls(was_answered_by_ritsu)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_calls_spam ON calls(is_spam)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_calls_priority ON calls(priority)")
    }
}