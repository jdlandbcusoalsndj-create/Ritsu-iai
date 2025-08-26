package com.ritsu.ai.assistant.service

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.ritsu.ai.assistant.RitsuApplication
import com.ritsu.ai.assistant.data.database.entity.Conversation
import com.ritsu.ai.assistant.data.database.entity.Contact
import com.ritsu.ai.assistant.data.database.ConversationType
import com.ritsu.ai.assistant.util.PreferenceManager
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

class AIService(private val context: Context) {
    
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false
    
    // Modelos de IA
    private var llamaModel: Any? = null
    private var whisperModel: Any? = null
    private var sentimentModel: Any? = null
    
    // Configuración
    private val preferenceManager = RitsuApplication.preferenceManager
    private val conversationRepository = RitsuApplication.conversationRepository
    private val contactRepository = RitsuApplication.contactRepository
    
    suspend fun initialize() {
        if (isInitialized) return
        
        try {
            Timber.d("Initializing AI Service...")
            
            // Inicializar Text-to-Speech
            initializeTextToSpeech()
            
            // Descargar modelos de IA si no existen
            downloadAIModels()
            
            // Cargar modelos
            loadAIModels()
            
            isInitialized = true
            Timber.d("AI Service initialized successfully")
            
        } catch (e: Exception) {
            Timber.e(e, "Error initializing AI Service")
            throw e
        }
    }
    
    private fun initializeTextToSpeech() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale("es"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Timber.w("Language not supported, falling back to English")
                    textToSpeech?.setLanguage(Locale.US)
                }
                
                textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        Timber.d("TTS started: $utteranceId")
                    }
                    
                    override fun onDone(utteranceId: String?) {
                        Timber.d("TTS completed: $utteranceId")
                    }
                    
                    override fun onError(utteranceId: String?) {
                        Timber.e("TTS error: $utteranceId")
                    }
                })
                
                Timber.d("Text-to-Speech initialized successfully")
            } else {
                Timber.e("Text-to-Speech initialization failed")
            }
        }
    }
    
    private suspend fun downloadAIModels() {
        val modelsDir = File(context.filesDir, "ai_models")
        if (!modelsDir.exists()) {
            modelsDir.mkdirs()
        }
        
        // Lista de modelos a descargar
        val models = listOf(
            AIModelInfo("llama-2-7b-chat.gguf", "https://huggingface.co/TheBloke/Llama-2-7B-Chat-GGUF/resolve/main/llama-2-7b-chat.Q4_K_M.gguf"),
            AIModelInfo("whisper-small.gguf", "https://huggingface.co/ggerganov/whisper.cpp/resolve/main/ggml-small.bin"),
            AIModelInfo("sentiment-analysis.pt", "https://huggingface.co/cardiffnlp/twitter-roberta-base-sentiment-latest/resolve/main/pytorch_model.bin")
        )
        
        models.forEach { modelInfo ->
            val modelFile = File(modelsDir, modelInfo.filename)
            if (!modelFile.exists()) {
                downloadModel(modelInfo.url, modelFile)
            }
        }
    }
    
    private suspend fun downloadModel(url: String, file: File) {
        withContext(Dispatchers.IO) {
            try {
                Timber.d("Downloading model: ${file.name}")
                
                val connection = URL(url).openConnection()
                val fileSize = connection.contentLength
                
                val inputStream = connection.getInputStream()
                val outputStream = FileOutputStream(file)
                
                val buffer = ByteArray(8192)
                var bytesRead: Int
                var totalBytesRead = 0L
                
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                    totalBytesRead += bytesRead
                    
                    val progress = ((totalBytesRead.toFloat() / fileSize) * 100).toInt()
                    Timber.d("Download progress for ${file.name}: $progress%")
                }
                
                inputStream.close()
                outputStream.close()
                
                Timber.d("Model downloaded successfully: ${file.name}")
                
            } catch (e: Exception) {
                Timber.e(e, "Error downloading model: ${file.name}")
                throw e
            }
        }
    }
    
    private suspend fun loadAIModels() {
        // Aquí se cargarían los modelos usando las librerías correspondientes
        // Por ahora, simulamos la carga
        Timber.d("Loading AI models...")
        delay(1000) // Simulación de carga
        Timber.d("AI models loaded successfully")
    }
    
    suspend fun processConversation(
        userMessage: String,
        contactId: Long? = null,
        context: String? = null
    ): String {
        return withContext(Dispatchers.IO) {
            try {
                Timber.d("Processing conversation: $userMessage")
                
                // Obtener contexto del contacto si existe
                val contact = contactId?.let { contactRepository.getContactById(it) }
                val contactContext = contact?.let { buildContactContext(it) }
                
                // Construir prompt completo
                val fullPrompt = buildPrompt(userMessage, contactContext, context)
                
                // Generar respuesta usando el modelo de IA
                val response = generateAIResponse(fullPrompt)
                
                // Guardar conversación en la base de datos
                val conversation = Conversation(
                    type = ConversationType.TEXT,
                    contactId = contactId,
                    userMessage = userMessage,
                    ritsuResponse = response,
                    contextData = context
                )
                
                conversationRepository.insertConversation(conversation)
                
                // Actualizar datos de interacción del contacto
                contact?.let {
                    contactRepository.updateInteractionData(it.id, java.time.LocalDateTime.now())
                }
                
                Timber.d("Conversation processed successfully")
                response
                
            } catch (e: Exception) {
                Timber.e(e, "Error processing conversation")
                "Lo siento, tuve un problema procesando tu mensaje. ¿Podrías intentarlo de nuevo?"
            }
        }
    }
    
    private fun buildContactContext(contact: Contact): String {
        return buildString {
            append("Contacto: ${contact.name}")
            contact.relationship?.let { append(", Relación: $it") }
            contact.importantNotes?.let { append(", Notas importantes: $it") }
            append(", Personalidad de Ritsu: ${contact.ritsuPersonality}")
            contact.customGreeting?.let { append(", Saludo personalizado: $it") }
        }
    }
    
    private fun buildPrompt(userMessage: String, contactContext: String?, additionalContext: String?): String {
        return buildString {
            appendLine("Eres Ritsu, una asistente de IA amigable y útil. Responde de manera natural y conversacional.")
            appendLine("Personalidad: Amigable, útil, algo juguetona, como una asistente personal.")
            appendLine("Idioma: Responde en español.")
            
            contactContext?.let {
                appendLine("Contexto del contacto: $it")
            }
            
            additionalContext?.let {
                appendLine("Contexto adicional: $it")
            }
            
            appendLine("Mensaje del usuario: $userMessage")
            appendLine("Respuesta de Ritsu:")
        }
    }
    
    private suspend fun generateAIResponse(prompt: String): String {
        // Aquí se usaría el modelo LLaMA real
        // Por ahora, simulamos una respuesta
        return withContext(Dispatchers.IO) {
            delay(500) // Simulación de procesamiento
            
            // Respuestas simuladas basadas en el prompt
            when {
                prompt.contains("hola", ignoreCase = true) -> "¡Hola! ¿Cómo estás hoy? 😊"
                prompt.contains("cómo estás", ignoreCase = true) -> "¡Muy bien, gracias! Estoy aquí para ayudarte con lo que necesites."
                prompt.contains("ayuda", ignoreCase = true) -> "¡Por supuesto! Puedo ayudarte con llamadas, mensajes, recordatorios y mucho más. ¿Qué necesitas?"
                prompt.contains("llamada", ignoreCase = true) -> "Puedo ayudarte a manejar llamadas. ¿Quieres que conteste por ti o necesitas hacer una llamada?"
                prompt.contains("mensaje", ignoreCase = true) -> "Puedo leer y responder mensajes por ti. ¿Qué te gustaría hacer?"
                else -> "Entiendo lo que dices. ¿En qué puedo ayudarte específicamente?"
            }
        }
    }
    
    fun speakText(text: String, utteranceId: String = "ritsu_speech") {
        textToSpeech?.let { tts ->
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        }
    }
    
    suspend fun handleIncomingCall(phoneNumber: String, contact: Contact? = null): String {
        return withContext(Dispatchers.IO) {
            try {
                val contactName = contact?.name ?: "Desconocido"
                val personality = contact?.ritsuPersonality ?: "friendly"
                
                val greeting = when (personality) {
                    "formal" -> "Buenos días, habla Ritsu, asistente personal. ¿En qué puedo ayudarle?"
                    "casual" -> "¡Hola! Soy Ritsu, ¿qué tal?"
                    else -> "¡Hola! Soy Ritsu, la asistente personal. ¿En qué puedo ayudarte?"
                }
                
                val context = "Llamada entrante de $contactName ($phoneNumber). Personalidad: $personality"
                
                // Guardar en base de datos
                val conversation = Conversation(
                    type = ConversationType.CALL,
                    contactId = contact?.id,
                    userMessage = "Llamada entrante",
                    ritsuResponse = greeting,
                    contextData = context
                )
                
                conversationRepository.insertConversation(conversation)
                
                greeting
                
            } catch (e: Exception) {
                Timber.e(e, "Error handling incoming call")
                "Hola, soy Ritsu. ¿En qué puedo ayudarte?"
            }
        }
    }
    
    suspend fun processCallConversation(
        callerMessage: String,
        contact: Contact?,
        callContext: String
    ): String {
        return withContext(Dispatchers.IO) {
            try {
                val personality = contact?.ritsuPersonality ?: "friendly"
                val context = "Conversación telefónica con ${contact?.name ?: "desconocido"}. Contexto: $callContext"
                
                val response = generateAIResponse("Mensaje del llamante: $callerMessage. Contexto: $context")
                
                // Guardar conversación
                val conversation = Conversation(
                    type = ConversationType.CALL,
                    contactId = contact?.id,
                    userMessage = callerMessage,
                    ritsuResponse = response,
                    contextData = context
                )
                
                conversationRepository.insertConversation(conversation)
                
                response
                
            } catch (e: Exception) {
                Timber.e(e, "Error processing call conversation")
                "Lo siento, no pude procesar eso. ¿Podrías repetirlo?"
            }
        }
    }
    
    suspend fun analyzeSentiment(text: String): Float {
        return withContext(Dispatchers.IO) {
            try {
                // Aquí se usaría el modelo de análisis de sentimientos real
                // Por ahora, simulamos un análisis básico
                val positiveWords = listOf("feliz", "contento", "genial", "excelente", "bueno", "me gusta", "amor")
                val negativeWords = listOf("triste", "malo", "terrible", "odio", "no me gusta", "problema", "error")
                
                val words = text.lowercase().split(" ")
                val positiveCount = words.count { it in positiveWords }
                val negativeCount = words.count { it in negativeWords }
                
                when {
                    positiveCount > negativeCount -> 0.7f
                    negativeCount > positiveCount -> 0.3f
                    else -> 0.5f
                }
                
            } catch (e: Exception) {
                Timber.e(e, "Error analyzing sentiment")
                0.5f
            }
        }
    }
    
    fun shutdown() {
        scope.cancel()
        textToSpeech?.shutdown()
        isInitialized = false
        Timber.d("AI Service shutdown")
    }
    
    private data class AIModelInfo(
        val filename: String,
        val url: String
    )
}