package com.bignerdranch.android.note.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Entity
import com.bignerdranch.android.note.database.NoteDatabase
import com.bignerdranch.android.note.model.Note
import com.bignerdranch.android.note.repository.FirebaseRepository
import com.bignerdranch.android.note.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val noteRepository = NoteRepository(NoteDatabase.getInstance(application))
    private val firebaseRepository = FirebaseRepository()

    fun insert(note: Note) {
        firebaseRepository.writeDatabase(note)
        viewModelScope.launch {
            noteRepository.insert(note)
        }
    }

}