package com.example.notepad

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.adapters.NoteAdapter
import com.example.notepad.databinding.ActivityMainBinding
import com.example.notepad.models.Note
import kotlinx.coroutines.launch

/**
 * Main Activity for NotepadApp
 * Displays time slots (17:00-20:00, 15min intervals) and manages notes
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: NoteViewModel by viewModels()
    
    // Time slot configuration
    companion object {
        const val TIME_START_HOUR = 17
        const val TIME_END_HOUR = 20
        const val TIME_STEP_MINUTES = 15
        
        // Calculate total time slots (4 slots)
        private val TOTAL_TIME_SLOTS = ((TIME_END_HOUR - TIME_START_HOUR) * 60 + 
                                        (TIME_END_HOUR % 60 - TIME_START_HOUR % 60)) / TIME_STEP_MINUTES
        
        fun getTimeSlots(): List<String> {
            return (1..TOTAL_TIME_SLOTS).map { slot ->
                val hour = TIME_START_HOUR + ((it - 1) * TIME_STEP_MINUTES / 60)
                "${hour.toString().padStart(2, '0')}:00"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeNotes()
    }

    private fun setupRecyclerView() {
        val adapter = NoteAdapter(
            onNoteClick = { note ->
                // Handle note click (open, edit, etc.)
                viewModel.openNote(note.id)
            },
            onDeleteClick = { noteId ->
                lifecycleScope.launch {
                    viewModel.deleteNote(noteId)
                }
            }
        )

        binding.recyclerViewNotes.apply {
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                stackFromEnd = true // Stack from bottom for better UX
            }
            adapter = adapter
        }
    }

    private fun observeNotes() {
        viewModel.notes.observe(this) { notes ->
            binding.recyclerViewNotes.adapter?.notifyDataSetChanged()
            
            if (notes.isEmpty()) {
                showEmptyState()
            } else {
                hideEmptyState()
            }
        }
    }

    private fun showEmptyState() {
        binding.tvEmptyState.visibility = android.view.View.VISIBLE
        binding.recyclerViewNotes.visibility = android.view.View.GONE
    }

    private fun hideEmptyState() {
        binding.tvEmptyState.visibility = android.view.View.GONE
        binding.recyclerViewNotes.visibility = android.view.View.VISIBLE
    }
}