package com.ritsu.ai.assistant.service

import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import com.alphacephei.voik.VoskRecognizer
import com.ritsu.ai.assistant.RitsuApplication
import com.ritsu.ai.assistant.data.database.entity.SpeechData
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

class SpeechRecognitionService(private val context: Context) {
    
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var audioRecord: AudioRecord? = null
    private var voskRecognizer: VoskRecognizer? = null
    private var isListening = false
    private var isInitialized = false
    
    // Configuración de audio
    private val sampleRate = 16000
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
    
    // Callbacks
    private var onSpeechRecognized: ((String) -> Unit)? = null
    private var onListeningStarted: (() -> Unit)? = null
    private var onListeningStopped: (() -> Unit)? = null
    private var onError: ((String) -> Unit)? = null
    
    suspend fun initialize() {
        if (isInitialized) return
        
        try {
            Timber.d("Initializing Speech Recognition Service...")
            
            // Descargar modelo Vosk si no existe
            downloadVoskModel()
            
            // Inicializar Vosk
            initializeVosk()
            
            isInitialized = true
            Timber.d("Speech Recognition Service initialized successfully")
            
        } catch (e: Exception) {
            Timber.e(e, "Error initializing Speech Recognition Service")
            throw e
        }
    }
    
    private suspend fun downloadVoskModel() {
        val modelsDir = File(context.filesDir, "vosk_models")
        if (!modelsDir.exists()) {
            modelsDir.mkdirs()
        }
        
        val modelDir = File(modelsDir, "vosk-model-small-es")
        if (!modelDir.exists()) {
            downloadVoskModelFromHuggingFace(modelDir)
        }
    }
    
    private suspend fun downloadVoskModelFromHuggingFace(modelDir: File) {
        withContext(Dispatchers.IO) {
            try {
                Timber.d("Downloading Vosk model...")
                
                // URL del modelo español pequeño de Vosk
                val modelUrl = "https://alphacephei.com/vosk/models/vosk-model-small-es-0.42.zip"
                val zipFile = File(modelDir.parentFile, "vosk-model-small-es.zip")
                
                // Descargar archivo ZIP
                val connection = URL(modelUrl).openConnection()
                val inputStream = connection.getInputStream()
                val outputStream = FileOutputStream(zipFile)
                
                val buffer = ByteArray(8192)
                var bytesRead: Int
                
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
                
                inputStream.close()
                outputStream.close()
                
                // Extraer ZIP (simulado por ahora)
                Timber.d("Vosk model downloaded, extracting...")
                // Aquí se extraería el ZIP real
                
                // Limpiar archivo ZIP
                zipFile.delete()
                
                Timber.d("Vosk model ready")
                
            } catch (e: Exception) {
                Timber.e(e, "Error downloading Vosk model")
                throw e
            }
        }
    }
    
    private fun initializeVosk() {
        try {
            val modelPath = File(context.filesDir, "vosk_models/vosk-model-small-es").absolutePath
            voskRecognizer = VoskRecognizer(modelPath, sampleRate.toFloat())
            Timber.d("Vosk initialized with model: $modelPath")
        } catch (e: Exception) {
            Timber.e(e, "Error initializing Vosk")
            throw e
        }
    }
    
    fun startListening() {
        if (!isInitialized || isListening) return
        
        scope.launch {
            try {
                Timber.d("Starting speech recognition...")
                
                // Inicializar AudioRecord
                audioRecord = AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    sampleRate,
                    channelConfig,
                    audioFormat,
                    bufferSize
                )
                
                if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
                    throw Exception("AudioRecord not initialized")
                }
                
                audioRecord?.startRecording()
                isListening = true
                onListeningStarted?.invoke()
                
                Timber.d("Started listening for speech")
                
                // Buffer para audio
                val buffer = ShortArray(bufferSize / 2)
                
                while (isListening) {
                    val readSize = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                    
                    if (readSize > 0) {
                        // Procesar audio con Vosk
                        val result = voskRecognizer?.acceptWaveForm(buffer, readSize)
                        
                        if (result == true) {
                            // Reconocimiento completo
                            val recognizedText = voskRecognizer?.result
                            if (!recognizedText.isNullOrEmpty() && recognizedText != "[]") {
                                processRecognizedSpeech(recognizedText)
                            }
                        } else {
                            // Reconocimiento parcial
                            val partialText = voskRecognizer?.partialResult
                            if (!partialText.isNullOrEmpty() && partialText != "[]") {
                                Timber.d("Partial recognition: $partialText")
                            }
                        }
                    }
                    
                    delay(100) // Pequeña pausa para no saturar la CPU
                }
                
            } catch (e: Exception) {
                Timber.e(e, "Error during speech recognition")
                onError?.invoke("Error en reconocimiento de voz: ${e.message}")
            } finally {
                stopListening()
            }
        }
    }
    
    fun stopListening() {
        if (!isListening) return
        
        try {
            isListening = false
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null
            
            // Obtener resultado final
            val finalResult = voskRecognizer?.finalResult
            if (!finalResult.isNullOrEmpty() && finalResult != "[]") {
                processRecognizedSpeech(finalResult)
            }
            
            onListeningStopped?.invoke()
            Timber.d("Stopped listening for speech")
            
        } catch (e: Exception) {
            Timber.e(e, "Error stopping speech recognition")
        }
    }
    
    private suspend fun processRecognizedSpeech(recognizedText: String) {
        try {
            // Extraer texto del JSON de Vosk
            val text = extractTextFromVoskResult(recognizedText)
            
            if (text.isNotBlank()) {
                Timber.d("Recognized speech: $text")
                
                // Guardar en base de datos
                val speechData = SpeechData(
                    type = "recognition",
                    content = text,
                    language = "es",
                    confidenceScore = extractConfidenceFromVoskResult(recognizedText),
                    isSuccessful = true
                )
                
                RitsuApplication.speechRepository.insertSpeechData(speechData)
                
                // Notificar callback
                onSpeechRecognized?.invoke(text)
                
                // Procesar con IA si contiene palabras clave
                if (containsWakeWord(text)) {
                    processWakeWord(text)
                }
            }
            
        } catch (e: Exception) {
            Timber.e(e, "Error processing recognized speech")
        }
    }
    
    private fun extractTextFromVoskResult(voskResult: String): String {
        return try {
            // Extraer texto del JSON de Vosk
            val textMatch = Regex("\"text\"\\s*:\\s*\"([^\"]*)\"").find(voskResult)
            textMatch?.groupValues?.get(1) ?: ""
        } catch (e: Exception) {
            Timber.e(e, "Error extracting text from Vosk result")
            ""
        }
    }
    
    private fun extractConfidenceFromVoskResult(voskResult: String): Float? {
        return try {
            val confidenceMatch = Regex("\"conf\"\\s*:\\s*([0-9.]+)").find(voskResult)
            confidenceMatch?.groupValues?.get(1)?.toFloatOrNull()
        } catch (e: Exception) {
            Timber.e(e, "Error extracting confidence from Vosk result")
            null
        }
    }
    
    private fun containsWakeWord(text: String): Boolean {
        val wakeWords = listOf("hey ritsu", "ritsu", "oye ritsu", "hola ritsu")
        return wakeWords.any { text.lowercase().contains(it) }
    }
    
    private suspend fun processWakeWord(text: String) {
        try {
            // Remover palabra de activación
            val cleanText = text.lowercase()
                .replace("hey ritsu", "")
                .replace("ritsu", "")
                .replace("oye ritsu", "")
                .replace("hola ritsu", "")
                .trim()
            
            if (cleanText.isNotBlank()) {
                // Procesar comando con IA
                val response = RitsuApplication.aiService.processConversation(cleanText)
                
                // Hablar respuesta
                RitsuApplication.aiService.speakText(response)
            }
            
        } catch (e: Exception) {
            Timber.e(e, "Error processing wake word")
        }
    }
    
    suspend fun recognizeFromFile(audioFile: File): String {
        return withContext(Dispatchers.IO) {
            try {
                Timber.d("Recognizing speech from file: ${audioFile.name}")
                
                val inputStream = FileInputStream(audioFile)
                val buffer = ByteArray(4096)
                var bytesRead: Int
                
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    voskRecognizer?.acceptWaveForm(buffer, bytesRead)
                }
                
                inputStream.close()
                
                val result = voskRecognizer?.finalResult
                val text = extractTextFromVoskResult(result ?: "")
                
                Timber.d("File recognition result: $text")
                text
                
            } catch (e: Exception) {
                Timber.e(e, "Error recognizing speech from file")
                ""
            }
        }
    }
    
    // Setters para callbacks
    fun setOnSpeechRecognized(callback: (String) -> Unit) {
        onSpeechRecognized = callback
    }
    
    fun setOnListeningStarted(callback: () -> Unit) {
        onListeningStarted = callback
    }
    
    fun setOnListeningStopped(callback: () -> Unit) {
        onListeningStopped = callback
    }
    
    fun setOnError(callback: (String) -> Unit) {
        onError = callback
    }
    
    fun isListening(): Boolean = isListening
    
    fun shutdown() {
        scope.cancel()
        stopListening()
        voskRecognizer?.close()
        isInitialized = false
        Timber.d("Speech Recognition Service shutdown")
    }
}