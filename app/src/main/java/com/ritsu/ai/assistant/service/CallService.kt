package com.ritsu.ai.assistant.service

import android.content.Context
import android.media.AudioManager
import android.media.MediaRecorder
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import com.ritsu.ai.assistant.RitsuApplication
import com.ritsu.ai.assistant.data.database.entity.Call
import com.ritsu.ai.assistant.data.database.entity.Contact
import com.ritsu.ai.assistant.data.database.CallType
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File
import java.time.LocalDateTime

class CallService(private val context: Context) {
    
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var audioManager: AudioManager? = null
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private var currentCallId: Long? = null
    private var currentContact: Contact? = null
    private var callStartTime: LocalDateTime? = null
    
    // Callbacks
    private var onCallStarted: ((String, Contact?) -> Unit)? = null
    private var onCallEnded: ((Long, Int) -> Unit)? = null
    private var onCallAnswered: ((String) -> Unit)? = null
    private var onCallRejected: (() -> Unit)? = null
    private var onCallRecording: ((String) -> Unit)? = null
    
    init {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    
    suspend fun handleIncomingCall(phoneNumber: String, contact: Contact? = null) {
        try {
            Timber.d("Handling incoming call from: $phoneNumber")
            
            currentContact = contact
            callStartTime = LocalDateTime.now()
            
            // Crear registro de llamada
            val call = Call(
                type = CallType.INCOMING,
                phoneNumber = phoneNumber,
                contactId = contact?.id,
                timestamp = callStartTime!!
            )
            
            currentCallId = RitsuApplication.callRepository.insertCall(call)
            
            // Determinar si Ritsu debe contestar automáticamente
            val shouldAutoAnswer = contact?.autoAnswerEnabled == true
            
            if (shouldAutoAnswer) {
                answerCallWithRitsu(phoneNumber, contact)
            } else {
                // Preguntar al usuario si quiere que Ritsu conteste
                askUserToAnswer(phoneNumber, contact)
            }
            
            onCallStarted?.invoke(phoneNumber, contact)
            
        } catch (e: Exception) {
            Timber.e(e, "Error handling incoming call")
        }
    }
    
    private suspend fun askUserToAnswer(phoneNumber: String, contact: Contact?) {
        val contactName = contact?.name ?: "Desconocido"
        val message = "Llamada entrante de $contactName ($phoneNumber). ¿Quieres que Ritsu conteste por ti?"
        
        RitsuApplication.aiService.speakText(message)
        
        // Aquí se podría mostrar una notificación interactiva
        // Por ahora, simulamos que el usuario acepta después de 3 segundos
        delay(3000)
        answerCallWithRitsu(phoneNumber, contact)
    }
    
    private suspend fun answerCallWithRitsu(phoneNumber: String, contact: Contact?) {
        try {
            Timber.d("Ritsu answering call from: $phoneNumber")
            
            // Configurar audio para la llamada
            setupCallAudio()
            
            // Generar saludo personalizado
            val greeting = generatePersonalizedGreeting(contact)
            
            // Hablar el saludo
            RitsuApplication.aiService.speakText(greeting)
            
            // Iniciar grabación de la llamada
            startCallRecording()
            
            // Iniciar conversación con IA
            startAIConversation(contact)
            
            onCallAnswered?.invoke(greeting)
            
        } catch (e: Exception) {
            Timber.e(e, "Error answering call with Ritsu")
        }
    }
    
    private fun generatePersonalizedGreeting(contact: Contact?): String {
        val personality = contact?.ritsuPersonality ?: "friendly"
        val customGreeting = contact?.customGreeting
        
        return when {
            customGreeting != null -> customGreeting
            personality == "formal" -> "Buenos días, habla Ritsu, asistente personal. ¿En qué puedo ayudarle?"
            personality == "casual" -> "¡Hola! Soy Ritsu, ¿qué tal?"
            else -> "¡Hola! Soy Ritsu, la asistente personal. ¿En qué puedo ayudarte?"
        }
    }
    
    private fun setupCallAudio() {
        try {
            audioManager?.let { am ->
                // Configurar modo de llamada
                am.mode = AudioManager.MODE_IN_CALL
                
                // Activar altavoz si es necesario
                if (preferenceManager.isSpeakerEnabled()) {
                    am.isSpeakerphoneOn = true
                }
                
                // Ajustar volumen
                am.setStreamVolume(
                    AudioManager.STREAM_VOICE_CALL,
                    am.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL) / 2,
                    0
                )
            }
        } catch (e: Exception) {
            Timber.e(e, "Error setting up call audio")
        }
    }
    
    private fun startCallRecording() {
        try {
            val recordingFile = File(context.filesDir, "call_recording_${System.currentTimeMillis()}.mp3")
            
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(recordingFile.absolutePath)
                prepare()
                start()
            }
            
            isRecording = true
            Timber.d("Call recording started: ${recordingFile.name}")
            
        } catch (e: Exception) {
            Timber.e(e, "Error starting call recording")
        }
    }
    
    private suspend fun startAIConversation(contact: Contact?) {
        scope.launch {
            try {
                // Simular conversación en tiempo real
                // En una implementación real, esto se conectaría con el audio de la llamada
                
                var conversationActive = true
                var turnCount = 0
                
                while (conversationActive && turnCount < 10) { // Máximo 10 turnos por simplicidad
                    delay(2000) // Simular tiempo de respuesta
                    
                    // Simular mensaje del llamante
                    val callerMessage = generateSimulatedCallerMessage(contact, turnCount)
                    
                    // Procesar con IA
                    val response = RitsuApplication.aiService.processCallConversation(
                        callerMessage,
                        contact,
                        "Conversación telefónica en curso"
                    )
                    
                    // Hablar respuesta
                    RitsuApplication.aiService.speakText(response)
                    
                    // Guardar en base de datos
                    saveCallConversation(callerMessage, response)
                    
                    turnCount++
                    
                    // Simular fin de conversación
                    if (turnCount >= 5 && Math.random() < 0.3) {
                        conversationActive = false
                    }
                }
                
                // Finalizar llamada
                endCall()
                
            } catch (e: Exception) {
                Timber.e(e, "Error in AI conversation")
                endCall()
            }
        }
    }
    
    private fun generateSimulatedCallerMessage(contact: Contact?, turnCount: Int): String {
        return when (turnCount) {
            0 -> "Hola, ¿está disponible?"
            1 -> "Necesito hablar sobre el proyecto"
            2 -> "¿Cuándo podemos reunirnos?"
            3 -> "Perfecto, gracias por la información"
            4 -> "Hasta luego"
            else -> "Adiós"
        }
    }
    
    private suspend fun saveCallConversation(callerMessage: String, ritsuResponse: String) {
        try {
            val conversation = com.ritsu.ai.assistant.data.database.entity.Conversation(
                type = com.ritsu.ai.assistant.data.database.ConversationType.CALL,
                contactId = currentContact?.id,
                userMessage = callerMessage,
                ritsuResponse = ritsuResponse,
                contextData = "Conversación telefónica en curso"
            )
            
            RitsuApplication.conversationRepository.insertConversation(conversation)
            
        } catch (e: Exception) {
            Timber.e(e, "Error saving call conversation")
        }
    }
    
    fun rejectCall() {
        try {
            Timber.d("Rejecting call")
            
            // Actualizar estado de la llamada
            currentCallId?.let { callId ->
                scope.launch {
                    RitsuApplication.callRepository.updateCallType(callId, CallType.REJECTED)
                }
            }
            
            onCallRejected?.invoke()
            
        } catch (e: Exception) {
            Timber.e(e, "Error rejecting call")
        }
    }
    
    private fun endCall() {
        try {
            Timber.d("Ending call")
            
            // Detener grabación
            stopCallRecording()
            
            // Restaurar configuración de audio
            restoreAudioSettings()
            
            // Calcular duración
            val duration = callStartTime?.let { start ->
                java.time.Duration.between(start, LocalDateTime.now()).seconds.toInt()
            } ?: 0
            
            // Actualizar registro de llamada
            currentCallId?.let { callId ->
                scope.launch {
                    RitsuApplication.callRepository.updateCallDuration(callId, duration)
                    RitsuApplication.callRepository.updateCallAnsweredByRitsu(callId, true)
                }
            }
            
            // Generar resumen de la conversación
            generateCallSummary()
            
            onCallEnded?.invoke(currentCallId ?: 0, duration)
            
            // Limpiar estado
            currentCallId = null
            currentContact = null
            callStartTime = null
            
        } catch (e: Exception) {
            Timber.e(e, "Error ending call")
        }
    }
    
    private fun stopCallRecording() {
        try {
            if (isRecording) {
                mediaRecorder?.apply {
                    stop()
                    release()
                }
                mediaRecorder = null
                isRecording = false
                Timber.d("Call recording stopped")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error stopping call recording")
        }
    }
    
    private fun restoreAudioSettings() {
        try {
            audioManager?.apply {
                mode = AudioManager.MODE_NORMAL
                isSpeakerphoneOn = false
            }
        } catch (e: Exception) {
            Timber.e(e, "Error restoring audio settings")
        }
    }
    
    private suspend fun generateCallSummary() {
        try {
            currentCallId?.let { callId ->
                val conversations = RitsuApplication.conversationRepository.getConversationsByType(
                    com.ritsu.ai.assistant.data.database.ConversationType.CALL
                ).first()
                
                val recentConversations = conversations.filter { 
                    it.timestamp.isAfter(callStartTime?.minusMinutes(1) ?: LocalDateTime.now())
                }
                
                val summary = buildString {
                    appendLine("Resumen de la llamada:")
                    appendLine("Contacto: ${currentContact?.name ?: "Desconocido"}")
                    appendLine("Duración: ${callStartTime?.let { start ->
                        java.time.Duration.between(start, LocalDateTime.now()).seconds
                    } ?: 0} segundos")
                    appendLine("Temas discutidos:")
                    
                    recentConversations.forEach { conversation ->
                        appendLine("- ${conversation.userMessage}")
                    }
                }
                
                // Guardar resumen
                RitsuApplication.callRepository.updateCallSummary(callId, summary)
                
                Timber.d("Call summary generated: $summary")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error generating call summary")
        }
    }
    
    // Setters para callbacks
    fun setOnCallStarted(callback: (String, Contact?) -> Unit) {
        onCallStarted = callback
    }
    
    fun setOnCallEnded(callback: (Long, Int) -> Unit) {
        onCallEnded = callback
    }
    
    fun setOnCallAnswered(callback: (String) -> Unit) {
        onCallAnswered = callback
    }
    
    fun setOnCallRejected(callback: () -> Unit) {
        onCallRejected = callback
    }
    
    fun setOnCallRecording(callback: (String) -> Unit) {
        onCallRecording = callback
    }
    
    fun isInCall(): Boolean = currentCallId != null
    
    fun shutdown() {
        scope.cancel()
        stopCallRecording()
        restoreAudioSettings()
        Timber.d("Call Service shutdown")
    }
    
    private val preferenceManager = RitsuApplication.preferenceManager
}