package com.example.composebasics

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Light = lightColorScheme(
    primary = Color(0xFF7B2FE0),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE6DBFF),
    onPrimaryContainer = Color(0xFF1E1440),
    background = Color(0xFFF7F5FC),
    surface = Color(0xFFF7F5FC),
)
private val Dark = darkColorScheme(
    primary = Color(0xFFC9B3FF),
    onPrimary = Color(0xFF1E1440),
    primaryContainer = Color(0xFF4B2A8F),
    background = Color(0xFF141022),
    surface = Color(0xFF141022),
)

/** Your app's colours. Every screen inside it uses them through MaterialTheme.colorScheme. */
@Composable
fun ComposeBasicsTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) Dark else Light, content = content)
}
