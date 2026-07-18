package com.example.notepad.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.notepad.data.NotepadDatabase
import com.example.notepad.models.Note
import kotlinx.coroutines.launch

/**
 * ViewModel for managing notes and time slots
 */
class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val database = NotepadDatabase.getDatabase(application)
    private val noteDao = database.noteDao()
    
    // LiveData for observing notes list
    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    init {
        loadNotes()
    }

    /**
     * Load all notes from the database
     */
    private fun loadNotes() {
        viewModelScope.launch {
            try {
                val notes = noteDao.getAllNotes()
                _notes.value = notes
            } catch (e: Exception) {
                e.printStackTrace()
                _notes.value = emptyList()
            }
        }
    }

    /**
     * Insert a new note into the database
     */
    fun insertNote(note: Note) {
        viewModelScope.launch {
            try {
                noteDao.insert(note)
                loadNotes() // Refresh the list after insertion
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Update an existing note
     */
    fun updateNote(note: Note) {
        viewModelScope.launch {
            try {
                noteDao.update(note)
                loadNotes() // Refresh the list after update
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Delete a note by ID
     */
    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            try {
                // Find and delete the note
                val notes = noteDao.getAllNotes()
                val noteToDelete = notes.find { it.id == noteId }
                
                if (noteToDelete != null) {
                    noteDao.delete(noteToDelete)
                    loadNotes()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Get notes by time slot
     */
    fun getNotesByTimeSlot(timeSlot: Int): LiveData<List<Note>> {
        return object : androidx.lifecycle.LiveData<List<Note>>() {
            override fun observeForever(observer: android.arch.lifecycle.Observer<List<Note>>) {
                // Implementation for filtered observation
            }

            override fun removeObserver(observer: android.arch.lifecycle.Observer<List<Note>>) {
                super.removeObserver(observer)
            }

            override fun onChanged(value: List<Note>) {
                observer.onCallObserver(value)
            }
        }.also {
            // In production, use a proper filtered LiveData or observeAllNotes and filter in UI
        }
    }
}