package com.ritsu.ai.assistant.ui.launcher

import android.app.ActivityManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.ritsu.ai.assistant.RitsuApplication
import com.ritsu.ai.assistant.ui.components.RitsuAvatar
import com.ritsu.ai.assistant.ui.theme.RitsuTheme
import com.ritsu.ai.assistant.util.PreferenceManager
import kotlinx.coroutines.launch
import timber.log.Timber

class LauncherActivity : ComponentActivity() {
    
    private lateinit var preferenceManager: PreferenceManager
    private val scope = lifecycleScope
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Configurar pantalla completa
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        
        // Ocultar barra de navegación
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        
        preferenceManager = RitsuApplication.preferenceManager
        
        // Inicializar servicios
        initializeServices()
        
        setContent {
            RitsuTheme {
                LauncherScreen(
                    onAppClick = { appInfo -> launchApp(appInfo) },
                    onRitsuClick = { handleRitsuInteraction() },
                    onSettingsClick = { openSettings() }
                )
            }
        }
    }
    
    private fun initializeServices() {
        scope.launch {
            try {
                // Inicializar servicios de IA y reconocimiento de voz
                RitsuApplication.aiService.initialize()
                RitsuApplication.speechService.initialize()
                
                // Configurar callbacks de reconocimiento de voz
                RitsuApplication.speechService.setOnSpeechRecognized { text ->
                    handleVoiceCommand(text)
                }
                
                RitsuApplication.speechService.setOnListeningStarted {
                    Timber.d("Started listening for voice commands")
                }
                
                RitsuApplication.speechService.setOnListeningStopped {
                    Timber.d("Stopped listening for voice commands")
                }
                
                RitsuApplication.speechService.setOnError { error ->
                    Timber.e("Speech recognition error: $error")
                }
                
                // Iniciar escucha de voz
                RitsuApplication.speechService.startListening()
                
                // Saludo inicial de Ritsu
                val greeting = getInitialGreeting()
                RitsuApplication.aiService.speakText(greeting)
                
                Timber.d("Launcher services initialized successfully")
                
            } catch (e: Exception) {
                Timber.e(e, "Error initializing launcher services")
            }
        }
    }
    
    private fun getInitialGreeting(): String {
        val hour = java.time.LocalTime.now().hour
        return when {
            hour < 12 -> "¡Buenos días! Soy Ritsu, tu asistente personal. ¿En qué puedo ayudarte?"
            hour < 18 -> "¡Buenas tardes! Soy Ritsu, tu asistente personal. ¿En qué puedo ayudarte?"
            else -> "¡Buenas noches! Soy Ritsu, tu asistente personal. ¿En qué puedo ayudarte?"
        }
    }
    
    private fun handleRitsuInteraction() {
        scope.launch {
            try {
                val response = RitsuApplication.aiService.processConversation(
                    "Usuario tocó a Ritsu",
                    context = "Interacción táctil con avatar"
                )
                
                RitsuApplication.aiService.speakText(response)
                
            } catch (e: Exception) {
                Timber.e(e, "Error handling Ritsu interaction")
            }
        }
    }
    
    private fun handleVoiceCommand(command: String) {
        scope.launch {
            try {
                Timber.d("Processing voice command: $command")
                
                when {
                    command.contains("abrir", ignoreCase = true) -> {
                        val appName = command.replace("abrir", "").trim()
                        openAppByName(appName)
                    }
                    command.contains("configuración", ignoreCase = true) -> {
                        openSettings()
                    }
                    command.contains("ayuda", ignoreCase = true) -> {
                        val helpResponse = "Puedo ayudarte a abrir aplicaciones, hacer llamadas, enviar mensajes, y mucho más. Solo dime qué necesitas."
                        RitsuApplication.aiService.speakText(helpResponse)
                    }
                    else -> {
                        // Procesar con IA general
                        val response = RitsuApplication.aiService.processConversation(command)
                        RitsuApplication.aiService.speakText(response)
                    }
                }
                
            } catch (e: Exception) {
                Timber.e(e, "Error handling voice command")
            }
        }
    }
    
    private fun openAppByName(appName: String) {
        try {
            val packageManager = packageManager
            val intent = packageManager.getLaunchIntentForPackage(appName)
            
            if (intent != null) {
                startActivity(intent)
                RitsuApplication.aiService.speakText("Abriendo $appName")
            } else {
                // Buscar por nombre de aplicación
                val apps = getInstalledApps()
                val matchingApp = apps.find { 
                    it.label.lowercase().contains(appName.lowercase()) 
                }
                
                if (matchingApp != null) {
                    launchApp(matchingApp)
                } else {
                    RitsuApplication.aiService.speakText("No encontré la aplicación $appName")
                }
            }
            
        } catch (e: Exception) {
            Timber.e(e, "Error opening app: $appName")
            RitsuApplication.aiService.speakText("No pude abrir $appName")
        }
    }
    
    private fun launchApp(appInfo: AppInfo) {
        try {
            val intent = packageManager.getLaunchIntentForPackage(appInfo.packageName)
            if (intent != null) {
                startActivity(intent)
                RitsuApplication.aiService.speakText("Abriendo ${appInfo.label}")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error launching app: ${appInfo.packageName}")
        }
    }
    
    private fun openSettings() {
        try {
            val intent = Intent(this, Class.forName("com.ritsu.ai.assistant.ui.settings.SettingsActivity"))
            startActivity(intent)
        } catch (e: Exception) {
            Timber.e(e, "Error opening settings")
        }
    }
    
    private fun getInstalledApps(): List<AppInfo> {
        val packageManager = packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        
        val apps = mutableListOf<AppInfo>()
        
        try {
            val resolveInfoList = packageManager.queryIntentActivities(intent, 0)
            
            for (resolveInfo in resolveInfoList) {
                val appInfo = AppInfo(
                    label = resolveInfo.loadLabel(packageManager).toString(),
                    packageName = resolveInfo.activityInfo.packageName,
                    icon = resolveInfo.loadIcon(packageManager)
                )
                apps.add(appInfo)
            }
            
            // Ordenar por nombre
            apps.sortBy { it.label }
            
        } catch (e: Exception) {
            Timber.e(e, "Error getting installed apps")
        }
        
        return apps
    }
    
    override fun onResume() {
        super.onResume()
        
        // Reanudar escucha de voz
        if (RitsuApplication.speechService.isListening()) {
            RitsuApplication.speechService.startListening()
        }
    }
    
    override fun onPause() {
        super.onPause()
        
        // Pausar escucha de voz para ahorrar batería
        RitsuApplication.speechService.stopListening()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        
        // Limpiar recursos
        scope.launch {
            RitsuApplication.speechService.shutdown()
        }
    }
}

@Composable
fun LauncherScreen(
    onAppClick: (AppInfo) -> Unit,
    onRitsuClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val context = LocalContext.current
    val apps = remember { getInstalledApps(context) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
    ) {
        // Ritsu Avatar (centro de la pantalla)
        RitsuAvatar(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center),
            onClick = onRitsuClick
        )
        
        // Grid de aplicaciones (parte inferior)
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(apps.take(12)) { app ->
                AppIcon(
                    app = app,
                    onClick = { onAppClick(app) }
                )
            }
        }
        
        // Botón de configuración (esquina superior derecha)
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Settings,
                contentDescription = "Configuración",
                tint = Color.White
            )
        }
    }
}

@Composable
fun AppIcon(
    app: AppInfo,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.size(64.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16213E)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Aquí se mostraría el icono real de la aplicación
            Text(
                text = app.label.take(1).uppercase(),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun RitsuAvatar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Aquí se renderizaría el avatar 3D de Ritsu
            Text(
                text = "🎭",
                style = MaterialTheme.typography.displayLarge
            )
        }
    }
}

data class AppInfo(
    val label: String,
    val packageName: String,
    val icon: android.graphics.drawable.Drawable
)

fun getInstalledApps(context: android.content.Context): List<AppInfo> {
    val packageManager = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN, null)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    
    val apps = mutableListOf<AppInfo>()
    
    try {
        val resolveInfoList = packageManager.queryIntentActivities(intent, 0)
        
        for (resolveInfo in resolveInfoList) {
            val appInfo = AppInfo(
                label = resolveInfo.loadLabel(packageManager).toString(),
                packageName = resolveInfo.activityInfo.packageName,
                icon = resolveInfo.loadIcon(packageManager)
            )
            apps.add(appInfo)
        }
        
        apps.sortBy { it.label }
        
    } catch (e: Exception) {
        Timber.e(e, "Error getting installed apps")
    }
    
    return apps
}