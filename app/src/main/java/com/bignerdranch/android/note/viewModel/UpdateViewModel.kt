package com.bignerdranch.android.note.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.note.database.NoteDatabase
import com.bignerdranch.android.note.model.Note
import com.bignerdranch.android.note.repository.FirebaseRepository
import com.bignerdranch.android.note.repository.NoteRepository
import kotlinx.coroutines.launch

class UpdateViewModel(context: Application):AndroidViewModel(context) {

    private val noteRepository = NoteRepository(NoteDatabase.getInstance(context))
    private val firebaseRepository = FirebaseRepository()

    fun update(note: Note, oldDate: String)
    {
        viewModelScope.launch {
            firebaseRepository.update(note, oldDate)
            noteRepository.update(oldDate, note)
        }
    }
}