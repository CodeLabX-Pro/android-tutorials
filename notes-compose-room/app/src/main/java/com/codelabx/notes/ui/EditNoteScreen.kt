package com.codelabx.notes.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codelabx.notes.data.Note
import com.codelabx.notes.ui.theme.NoteColors
import com.codelabx.notes.ui.theme.NoteInk

/**
 * Create or edit a note. The whole screen takes the note's colour.
 * Leaving the screen (back arrow, tick, or the system Back gesture) saves the note.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(note: Note, onSave: (Note) -> Unit, onDelete: (Note) -> Unit) {
    // rememberSaveable keeps what you typed when the phone rotates.
    var title by rememberSaveable { mutableStateOf(note.title) }
    var content by rememberSaveable { mutableStateOf(note.content) }
    var color by rememberSaveable { mutableIntStateOf(note.color) }
    var pinned by rememberSaveable { mutableStateOf(note.pinned) }
    val save = { onSave(note.copy(title = title.trim(), content = content.trim(), color = color, pinned = pinned)) }
    BackHandler(onBack = save)

    val background = NoteColors[color % NoteColors.size]
    val clear = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedTextColor = NoteInk,
        unfocusedTextColor = NoteInk,
    )

    Scaffold(
        containerColor = background,
        contentColor = NoteInk,
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = background, navigationIconContentColor = NoteInk, actionIconContentColor = NoteInk),
                navigationIcon = {
                    IconButton(onClick = save) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back") }
                },
                actions = {
                    IconButton(onClick = { pinned = !pinned }) {
                        Icon(if (pinned) Icons.Filled.PushPin else Icons.Outlined.PushPin, contentDescription = if (pinned) "Unpin" else "Pin")
                    }
                    if (note.id != 0L) {
                        IconButton(onClick = { onDelete(note) }) { Icon(Icons.Outlined.Delete, contentDescription = "Delete") }
                    }
                    IconButton(onClick = save) { Icon(Icons.Filled.Check, contentDescription = "Save") }
                },
            )
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).imePadding()) {
            TextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Title", style = MaterialTheme.typography.headlineMedium, color = NoteInk.copy(alpha = 0.4f)) },
                textStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                colors = clear,
            )
            TextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier.fillMaxWidth().weight(1f),
                placeholder = { Text("Start writing…", color = NoteInk.copy(alpha = 0.4f)) },
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = clear,
            )
            ColorPicker(selected = color, onSelect = { color = it })
        }
    }
}

@Composable
private fun ColorPicker(selected: Int, onSelect: (Int) -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
    ) {
        NoteColors.forEachIndexed { index, swatch ->
            Box(
                Modifier
                    .size(40.dp)
                    .border(if (index == selected) 3.dp else 1.dp, NoteInk.copy(alpha = if (index == selected) 0.9f else 0.2f), CircleShape)
                    .padding(4.dp)
                    .background(swatch, CircleShape)
                    .clickable { onSelect(index) },
            )
        }
    }
}
