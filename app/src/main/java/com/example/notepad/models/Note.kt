package com.example.notepad.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Note model for Room database
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    // Time slot (0-3 representing 17:00, 17:15, 17:30, 17:45)
    val timeSlot: Int = 0,
    
    // Timestamp for ordering and updates
    val timestamp: Long = System.currentTimeMillis(),
    
    // Note content (text body)
    val content: String = "",
    
    // Title of the note
    val title: String = "",
    
    // Status flags
    val isCompleted: Boolean = false,
    val isDeleted: Boolean = false,
    
    // Metadata
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val TABLE_NAME = "notes"
        
        fun createNote(
            timeSlot: Int,
            content: String,
            title: String = ""
        ): Note {
            return Note(
                timeSlot = timeSlot,
                content = content,
                title = title,
                timestamp = System.currentTimeMillis(),
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        }

        fun updateNote(note: Note): Note {
            val updated = note.copy(
                updatedAt = System.currentTimeMillis()
            )
            return updated
        }
    }
}