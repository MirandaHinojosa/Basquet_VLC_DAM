package com.Miranda_JC.Basquet_Val.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColorScheme(
    primary = Color(0xFF006A65),       // Verde principal
    secondary = Color(0xFF4A6360),     // Verde secundario
    tertiary = Color(0xFF49607B),      // Azul para detalles
    background = Color(0xFFFAFDFB),    // Fondo claro
    surface = Color(0xFFFFFFFF),       // Superficie blanca
    onPrimary = Color(0xFFFFFFFF),     // Texto sobre verde
    onSecondary = Color(0xFFFFFFFF),   // Texto sobre verde secundario
    onBackground = Color(0xFF191C1C),  // Texto principal
    onSurface = Color(0xFF191C1C),     // Texto en superficie
    error = Color(0xFFBA1A1A)          // Rojo para errores
)

private val DarkColorPalette = darkColorScheme(
    primary = Color(0xFF4FDAD2),       // Verde claro
    secondary = Color(0xFFB0CCC8),     // Verde claro secundario
    tertiary = Color(0xFFA8C8E0),      // Azul claro
    background = Color(0xFF191C1C),    // Fondo oscuro
    surface = Color(0xFF2D3131),       // Superficie oscura
    onPrimary = Color(0xFF003734),     // Texto sobre verde claro
    onSecondary = Color(0xFF1B3532),   // Texto sobre verde secundario
    onBackground = Color(0xFFE0E3E3),  // Texto claro
    onSurface = Color(0xFFE0E3E3),     // Texto en superficie oscura
    error = Color(0xFFFFB4AB)          // Rojo suave
)

@Composable
fun BasquetListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}