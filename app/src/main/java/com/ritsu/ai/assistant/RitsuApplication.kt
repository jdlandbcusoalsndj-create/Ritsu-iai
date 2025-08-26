package com.ritsu.ai.assistant

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.Configuration
import androidx.work.WorkManager
import com.jakewharton.threetenabp.AndroidThreeTen
import com.ritsu.ai.assistant.data.database.RitsuDatabase
import com.ritsu.ai.assistant.data.repository.*
import com.ritsu.ai.assistant.service.AIService
import com.ritsu.ai.assistant.service.SpeechRecognitionService
import com.ritsu.ai.assistant.util.PreferenceManager
import com.ritsu.ai.assistant.util.TimberInitializer
import timber.log.Timber
import java.util.concurrent.Executors

class RitsuApplication : Application(), Configuration.Provider {

    companion object {
        lateinit var instance: RitsuApplication
            private set
        
        val database: RitsuDatabase by lazy { RitsuDatabase.getInstance(instance) }
        val preferenceManager: PreferenceManager by lazy { PreferenceManager(instance) }
        
        // Repositorios
        val conversationRepository: ConversationRepository by lazy { 
            ConversationRepository(database.conversationDao()) 
        }
        val contactRepository: ContactRepository by lazy { 
            ContactRepository(database.contactDao()) 
        }
        val messageRepository: MessageRepository by lazy { 
            MessageRepository(database.messageDao()) 
        }
        val callRepository: CallRepository by lazy { 
            CallRepository(database.callDao()) 
        }
        val aiModelRepository: AIModelRepository by lazy { 
            AIModelRepository(instance) 
        }
        val speechRepository: SpeechRepository by lazy { 
            SpeechRepository(instance) 
        }
        
        // Servicios
        val aiService: AIService by lazy { AIService(instance) }
        val speechService: SpeechRecognitionService by lazy { 
            SpeechRecognitionService(instance) 
        }
        
        // Executor para tareas en segundo plano
        val backgroundExecutor = Executors.newFixedThreadPool(4)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Inicializar Timber para logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        // Inicializar AndroidThreeTen para manejo de fechas
        AndroidThreeTen.init(this)
        
        // Crear canales de notificación
        createNotificationChannels()
        
        // Inicializar WorkManager
        WorkManager.initialize(this, workManagerConfiguration)
        
        // Inicializar servicios
        initializeServices()
        
        Timber.d("Ritsu AI Assistant initialized successfully")
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            // Canal para notificaciones de IA
            val aiChannel = NotificationChannel(
                CHANNEL_AI,
                "Ritsu AI",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones del asistente de IA Ritsu"
                enableVibration(true)
                enableLights(true)
            }
            
            // Canal para llamadas
            val callChannel = NotificationChannel(
                CHANNEL_CALLS,
                "Llamadas",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de llamadas telefónicas"
                enableVibration(true)
                enableLights(true)
            }
            
            // Canal para mensajes
            val messageChannel = NotificationChannel(
                CHANNEL_MESSAGES,
                "Mensajes",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones de mensajes"
                enableVibration(true)
            }
            
            // Canal para reconocimiento de voz
            val speechChannel = NotificationChannel(
                CHANNEL_SPEECH,
                "Reconocimiento de Voz",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Servicio de reconocimiento de voz"
                setShowBadge(false)
            }
            
            notificationManager.createNotificationChannels(
                listOf(aiChannel, callChannel, messageChannel, speechChannel)
            )
        }
    }

    private fun initializeServices() {
        try {
            // Inicializar servicios en segundo plano
            backgroundExecutor.execute {
                aiService.initialize()
                speechService.initialize()
            }
            
            Timber.d("Services initialized successfully")
        } catch (e: Exception) {
            Timber.e(e, "Error initializing services")
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(if (BuildConfig.DEBUG) android.util.Log.DEBUG else android.util.Log.ERROR)
            .setExecutor(backgroundExecutor)
            .build()
    }

    override fun onTerminate() {
        super.onTerminate()
        backgroundExecutor.shutdown()
        Timber.d("Ritsu AI Assistant terminated")
    }

    companion object {
        const val CHANNEL_AI = "ritsu_ai"
        const val CHANNEL_CALLS = "ritsu_calls"
        const val CHANNEL_MESSAGES = "ritsu_messages"
        const val CHANNEL_SPEECH = "ritsu_speech"
    }
}