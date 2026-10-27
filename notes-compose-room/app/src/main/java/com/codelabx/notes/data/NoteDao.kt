package com.codelabx.notes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * How the app reads and writes notes. Room writes the SQL behind these functions.
 * Queries that return a Flow update automatically whenever the table changes.
 */
@Dao
interface NoteDao {
    /** Pinned notes first, then the most recently edited. An empty [query] matches everything. */
    @Query(
        """
        SELECT * FROM notes
        WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'
        ORDER BY pinned DESC, updatedAt DESC
        """
    )
    fun observe(query: String): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun get(id: Long): Note?

    /** Inserts a new note (id = 0) or updates an existing one. */
    @Upsert
    suspend fun upsert(note: Note): Long

    @Delete
    suspend fun delete(note: Note)
}
