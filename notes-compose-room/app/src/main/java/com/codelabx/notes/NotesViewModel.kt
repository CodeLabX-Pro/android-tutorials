package com.codelabx.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.codelabx.notes.data.Note
import com.codelabx.notes.data.NoteDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Holds the screen state and talks to the database, so the UI only draws.
 * It survives screen rotation, and its coroutines stop when the screen is gone.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = NoteDatabase.get(application).noteDao()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    /** The notes to show. Re-runs the search whenever the query changes. */
    val notes: StateFlow<List<Note>> = _query
        .flatMapLatest { dao.observe(it.trim()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onQueryChange(text: String) {
        _query.value = text
    }

    suspend fun load(id: Long): Note? = dao.get(id)

    fun save(note: Note) = viewModelScope.launch {
        // Don't keep completely empty notes.
        if (note.title.isBlank() && note.content.isBlank()) return@launch
        dao.upsert(note.copy(updatedAt = System.currentTimeMillis()))
    }

    fun togglePin(note: Note) = viewModelScope.launch { dao.upsert(note.copy(pinned = !note.pinned)) }

    fun delete(note: Note) = viewModelScope.launch { dao.delete(note) }

    /** Puts a deleted note back, for the Undo button. */
    fun restore(note: Note) = viewModelScope.launch { dao.upsert(note) }
}
