package com.bignerdranch.android.note.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.bignerdranch.android.note.database.NoteDatabase
import com.bignerdranch.android.note.model.Note
import com.bignerdranch.android.note.model.Response
import com.bignerdranch.android.note.repository.FirebaseRepository
import com.bignerdranch.android.note.repository.NoteRepository
import com.google.firebase.FirebaseNetworkException
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val noteRepository = NoteRepository(NoteDatabase.getInstance(application))
    private val firebaseRepository = FirebaseRepository()

    var _notes = MutableLiveData<List<Note>>()

    fun getNote(position: Int): Note {
        return _notes.value?.get(position)!!
    }

    fun getNotes() {
        viewModelScope.launch {
            _notes.value = noteRepository.getAll()
        }
    }

    fun updateNotes(newNote: List<Note>) {
        _notes.value = newNote
    }

    fun getFirebaseNotes(): MutableLiveData<Response> {
        return firebaseRepository.readDatabase()
    }

    fun search(data: String): LiveData<List<Note>> {
        val searchNote = MutableLiveData<List<Note>>()
        viewModelScope.launch {
            searchNote.value = noteRepository.search(data)
        }
        return searchNote
    }

    fun delete(position: Int) {
        viewModelScope.launch {
            noteRepository.delete(_notes.value!!.get(position))
            firebaseRepository.deleteNews(_notes.value!!.get(position))
            getNotes()
        }
    }

    fun syncToFirebase() {
        viewModelScope.launch {
            firebaseRepository.deleteAll()
            val notes = noteRepository.getAll()
            for (note in notes) {
                firebaseRepository.writeDatabase(note)
            }
        }
    }

    fun sincToLocalBD() {
        viewModelScope.launch {
            noteRepository.deleteAll()
        }
        for (note in _notes.value!!) {
            viewModelScope.launch {
                noteRepository.insert(note)
            }
        }
    }
}