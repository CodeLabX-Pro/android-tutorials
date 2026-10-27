package com.codelabx.notes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One note. @Entity makes Room create a "notes" table with a column for each property.
 * [color] is an index into the note colours in the theme, so the palette can change later.
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val color: Int = 0,
    val pinned: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis(),
)
