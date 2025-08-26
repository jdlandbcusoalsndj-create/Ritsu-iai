package com.ritsu.ai.assistant.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta de colores anime de Ritsu
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF69B4), // Rosa anime
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE91E63),
    onPrimaryContainer = Color.White,
    
    secondary = Color(0xFF87CEEB), // Azul claro
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF4FC3F7),
    onSecondaryContainer = Color.White,
    
    tertiary = Color(0xFFFFB6C1), // Rosa claro
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFFF8A80),
    onTertiaryContainer = Color.White,
    
    background = Color(0xFF1A1A2E), // Azul oscuro
    onBackground = Color.White,
    surface = Color(0xFF16213E), // Azul medio
    onSurface = Color.White,
    
    surfaceVariant = Color(0xFF0F3460), // Azul más oscuro
    onSurfaceVariant = Color(0xFFE0E0E0),
    
    outline = Color(0xFFE91E63),
    outlineVariant = Color(0xFF4FC3F7),
    
    error = Color(0xFFFF6B6B),
    onError = Color.White,
    errorContainer = Color(0xFFD32F2F),
    onErrorContainer = Color.White,
    
    scrim = Color(0x80000000)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFE91E63), // Rosa más oscuro para modo claro
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFF69B4),
    onPrimaryContainer = Color.Black,
    
    secondary = Color(0xFF4FC3F7), // Azul más oscuro
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF87CEEB),
    onSecondaryContainer = Color.Black,
    
    tertiary = Color(0xFFFF8A80), // Rosa más oscuro
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFB6C1),
    onTertiaryContainer = Color.Black,
    
    background = Color(0xFFF8F9FA), // Gris muy claro
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    
    surfaceVariant = Color(0xFFE3F2FD), // Azul muy claro
    onSurfaceVariant = Color.Black,
    
    outline = Color(0xFFE91E63),
    outlineVariant = Color(0xFF4FC3F7),
    
    error = Color(0xFFD32F2F),
    onError = Color.White,
    errorContainer = Color(0xFFFF6B6B),
    onErrorContainer = Color.White,
    
    scrim = Color(0x80000000)
)

@Composable
fun RitsuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && darkTheme -> dynamicDarkColorScheme()
        dynamicColor && !darkTheme -> dynamicLightColorScheme()
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = RitsuTypography,
        content = content
    )
}

// Colores personalizados para componentes específicos
object RitsuColors {
    val RitsuPink = Color(0xFFFF69B4)
    val RitsuBlue = Color(0xFF87CEEB)
    val RitsuPurple = Color(0xFF9C27B0)
    val RitsuOrange = Color(0xFFFF9800)
    val RitsuGreen = Color(0xFF4CAF50)
    
    val BackgroundGradientStart = Color(0xFF1A1A2E)
    val BackgroundGradientEnd = Color(0xFF16213E)
    
    val CardBackground = Color(0xFF0F3460)
    val CardBorder = Color(0xFFE91E63)
    
    val TextPrimary = Color.White
    val TextSecondary = Color(0xFFE0E0E0)
    val TextTertiary = Color(0xFFBDBDBD)
    
    val Success = Color(0xFF4CAF50)
    val Warning = Color(0xFFFF9800)
    val Error = Color(0xFFFF6B6B)
    val Info = Color(0xFF4FC3F7)
}