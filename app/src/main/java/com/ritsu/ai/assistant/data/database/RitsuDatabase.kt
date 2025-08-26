package com.ritsu.ai.assistant.data.database

import android.content.Context
import androidx.room.*
import com.ritsu.ai.assistant.data.database.dao.*
import com.ritsu.ai.assistant.data.database.entity.*
import com.ritsu.ai.assistant.data.database.migration.Migration1To2
import com.ritsu.ai.assistant.data.database.migration.Migration2To3
import timber.log.Timber

@Database(
    entities = [
        Conversation::class,
        Contact::class,
        Message::class,
        Call::class,
        AIModel::class,
        UserPreference::class,
        RitsuState::class,
        SpeechData::class
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class RitsuDatabase : RoomDatabase() {

    abstract fun conversationDao(): ConversationDao
    abstract fun contactDao(): ContactDao
    abstract fun messageDao(): MessageDao
    abstract fun callDao(): CallDao
    abstract fun aiModelDao(): AIModelDao
    abstract fun userPreferenceDao(): UserPreferenceDao
    abstract fun ritsuStateDao(): RitsuStateDao
    abstract fun speechDataDao(): SpeechDataDao

    companion object {
        @Volatile
        private var INSTANCE: RitsuDatabase? = null

        fun getInstance(context: Context): RitsuDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RitsuDatabase::class.java,
                    "ritsu_database"
                )
                .addMigrations(Migration1To2, Migration2To3)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Timber.d("Database created successfully")
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        Timber.d("Database opened successfully")
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Converters para tipos de datos especiales
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): java.time.LocalDateTime? {
        return value?.let { java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(it), java.time.ZoneId.systemDefault()) }
    }

    @TypeConverter
    fun dateToTimestamp(date: java.time.LocalDateTime?): Long? {
        return date?.atZone(java.time.ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun fromStringList(value: String?): List<String> {
        return value?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
    }

    @TypeConverter
    fun toStringList(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun fromConversationType(value: ConversationType): String {
        return value.name
    }

    @TypeConverter
    fun toConversationType(value: String): ConversationType {
        return ConversationType.valueOf(value)
    }

    @TypeConverter
    fun fromMessageType(value: MessageType): String {
        return value.name
    }

    @TypeConverter
    fun toMessageType(value: String): MessageType {
        return MessageType.valueOf(value)
    }

    @TypeConverter
    fun fromCallType(value: CallType): String {
        return value.name
    }

    @TypeConverter
    fun toCallType(value: String): CallType {
        return CallType.valueOf(value)
    }

    @TypeConverter
    fun fromRitsuMood(value: RitsuMood): String {
        return value.name
    }

    @TypeConverter
    fun toRitsuMood(value: String): RitsuMood {
        return RitsuMood.valueOf(value)
    }
}

// Enums para tipos de datos
enum class ConversationType {
    VOICE, TEXT, CALL, MESSAGE
}

enum class MessageType {
    SMS, WHATSAPP, TELEGRAM, EMAIL
}

enum class CallType {
    INCOMING, OUTGOING, MISSED, REJECTED
}

enum class RitsuMood {
    HAPPY, SAD, THINKING, TALKING, LISTENING, NEUTRAL, EXCITED, CALM
}