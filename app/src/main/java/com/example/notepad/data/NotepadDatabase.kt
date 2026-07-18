package com.example.notepad.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.notepad.models.Note

/**
 * Room Database for storing notes and time slots
 */
@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class NotepadDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NotepadDatabase? = null

        fun getDatabase(context: Context): NotepadDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotepadDatabase::class.java,
                    "notepad_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

/**
 * DAO for Note operations
 */
interface NoteDao {
    @androidx.room.Query("SELECT * FROM notes ORDER BY timestamp DESC")
    suspend fun getAllNotes(): List<Note>

    @androidx.room.Insert
    suspend fun insert(note: Note)

    @androidx.room.Update
    suspend fun update(note: Note)

    @androidx.room.Delete
    suspend fun delete(note: Note)

    @androidx.room.Query("SELECT * FROM notes WHERE timeSlot = :timeSlot ORDER BY timestamp DESC")
    suspend fun getNotesByTimeSlot(timeSlot: Int): List<Note>

    @androidx.room.Query("DELETE FROM notes WHERE timeSlot = :timeSlot")
    suspend fun deleteAllByTimeSlot(timeSlot: Int)
}