package com.ritsu.ai.assistant.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val Migration2To3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Agregar nuevas columnas a la tabla ai_models
        database.execSQL("ALTER TABLE ai_models ADD COLUMN performance_score REAL")
        database.execSQL("ALTER TABLE ai_models ADD COLUMN last_used INTEGER")
        database.execSQL("ALTER TABLE ai_models ADD COLUMN usage_count INTEGER NOT NULL DEFAULT 0")
        
        // Agregar nuevas columnas a la tabla user_preferences
        database.execSQL("ALTER TABLE user_preferences ADD COLUMN is_system INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE user_preferences ADD COLUMN created_at INTEGER")
        database.execSQL("ALTER TABLE user_preferences ADD COLUMN updated_at INTEGER")
        
        // Agregar nuevas columnas a la tabla ritsu_states
        database.execSQL("ALTER TABLE ritsu_states ADD COLUMN energy_level REAL NOT NULL DEFAULT 1.0")
        database.execSQL("ALTER TABLE ritsu_states ADD COLUMN interaction_count_today INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE ritsu_states ADD COLUMN last_interaction INTEGER")
        database.execSQL("ALTER TABLE ritsu_states ADD COLUMN current_context TEXT")
        database.execSQL("ALTER TABLE ritsu_states ADD COLUMN custom_data TEXT")
        
        // Agregar nuevas columnas a la tabla speech_data
        database.execSQL("ALTER TABLE speech_data ADD COLUMN confidence_score REAL")
        database.execSQL("ALTER TABLE speech_data ADD COLUMN duration_seconds REAL")
        database.execSQL("ALTER TABLE speech_data ADD COLUMN audio_file_path TEXT")
        database.execSQL("ALTER TABLE speech_data ADD COLUMN model_used TEXT")
        database.execSQL("ALTER TABLE speech_data ADD COLUMN processing_time_ms INTEGER")
        database.execSQL("ALTER TABLE speech_data ADD COLUMN is_successful INTEGER NOT NULL DEFAULT 1")
        database.execSQL("ALTER TABLE speech_data ADD COLUMN error_message TEXT")
        database.execSQL("ALTER TABLE speech_data ADD COLUMN metadata TEXT")
        
        // Crear índices adicionales para mejorar el rendimiento
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_ai_models_performance ON ai_models(performance_score)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_ai_models_usage ON ai_models(usage_count)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_user_preferences_system ON user_preferences(is_system)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_ritsu_states_energy ON ritsu_states(energy_level)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_ritsu_states_interactions ON ritsu_states(interaction_count_today)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_speech_data_confidence ON speech_data(confidence_score)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_speech_data_success ON speech_data(is_successful)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_speech_data_model ON speech_data(model_used)")
        
        // Crear índices compuestos para consultas frecuentes
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_conversations_contact_type ON conversations(contact_id, type)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_messages_contact_type ON messages(contact_id, type)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_calls_contact_type ON calls(contact_id, type)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_speech_data_type_language ON speech_data(type, language)")
        
        // Agregar restricciones de integridad referencial
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_conversations_contact_fk ON conversations(contact_id)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_messages_contact_fk ON messages(contact_id)")
        database.execSQL("CREATE INDEX IF NOT EXISTS idx_calls_contact_fk ON calls(contact_id)")
        
        // Optimizar tablas existentes
        database.execSQL("ANALYZE")
    }
}