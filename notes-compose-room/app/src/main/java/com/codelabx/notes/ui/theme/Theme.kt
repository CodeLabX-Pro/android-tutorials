package com.codelabx.notes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** The note colours, chosen in the editor. A note stores the index, not the colour. */
val NoteColors = listOf(
    Color(0xFFFFF4C7), // butter
    Color(0xFFFFE1DB), // peach
    Color(0xFFE6DBFF), // lavender
    Color(0xFFD7F2FF), // sky
    Color(0xFFD9F7E6), // mint
    Color(0xFFFFFFFF), // paper
)
val NoteInk = Color(0xFF1E1440)

private val Light = lightColorScheme(
    primary = Color(0xFF7B2FE0),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE6DBFF),
    onPrimaryContainer = NoteInk,
    background = Color(0xFFF7F5FC),
    surface = Color(0xFFF7F5FC),
    surfaceContainerHigh = Color(0xFFEDE8F7),
)
private val Dark = darkColorScheme(
    primary = Color(0xFFC9B3FF),
    onPrimary = NoteInk,
    primaryContainer = Color(0xFF4B2A8F),
    background = Color(0xFF141022),
    surface = Color(0xFF141022),
    surfaceContainerHigh = Color(0xFF231C38),
)

@Composable
fun NotesTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) Dark else Light, content = content)
}
