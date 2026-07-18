package com.example.notepad

import android.app.Application
import androidx.room.Room
import java.io.File

/**
 * Application class for NotepadApp
 * Initializes database and application-wide components
 */
class NotepadApplication : Application() {

    companion object {
        private const val DB_NAME = "notepad_db"
        
        // Time slot configuration constants
        const val TIME_START_HOUR = 17
        const val TIME_START_MINUTE = 0
        const val TIME_END_HOUR = 20
        const val TIME_END_MINUTE = 0
        const val TIME_STEP_MINUTES = 15
        
        // Calculate total time slots (4 slots: 17:00, 17:15, 17:30, 17:45)
        private val TOTAL_TIME_SLOTS = ((TIME_END_HOUR - TIME_START_HOUR) * 60 + 
                                        (TIME_END_MINUTE - TIME_START_MINUTE)) / TIME_STEP_MINUTES
        
        // Database instance
        lateinit var database: NotepadDatabase
            private set

        fun getDatabase(context: Context): NotepadDatabase {
            return if (!this::database.isInitialized) {
                Room.databaseBuilder(
                    context.applicationContext,
                    NotepadDatabase::class.java,
                    DB_NAME
                ).build()
            } else {
                database
            }
        }

        fun getTimeSlotsCount(): Int = TOTAL_TIME_SLOTS
    }

    override fun onCreate() {
        super.onCreate()
        
        // Initialize database
        database = Room.databaseBuilder(
            applicationContext,
            NotepadDatabase::class.java,
            DB_NAME
        ).build()

        // Create data directory for file storage if needed
        val dataDir = File(applicationContext.filesDir, "data")
        if (!dataDir.exists()) {
            dataDir.mkdirs()
        }
    }
}