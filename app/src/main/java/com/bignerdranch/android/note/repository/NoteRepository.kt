package com.bignerdranch.android.note.repository

import androidx.lifecycle.LiveData
import com.bignerdranch.android.note.database.NoteDatabase
import com.bignerdranch.android.note.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.w3c.dom.Node

class NoteRepository(private val database: NoteDatabase) {

    suspend fun insert(note: Note) {
        withContext(Dispatchers.IO) {
            database.getDao().insert(note)
        }
    }

    suspend fun getAll(): List<Note> =
        withContext(Dispatchers.IO) {
            return@withContext database.getDao().getAll()
        }

    suspend fun updateAll(list: List<Note>){
        withContext(Dispatchers.IO) {
//            for (note in list){
//                if(database.getDao().searchById(note.data) == null){
//                    database.getDao().insert(note)
//                }
//            }
        }
    }

    suspend fun deleteAll(){
        withContext(Dispatchers.IO) {
            database.getDao().deleteAll()
        }
    }

    suspend fun update(oldDate:String, note: Note) {
        withContext(Dispatchers.IO) {
            database.getDao().update(oldDate, note.characters, note.date, note.data)
        }
    }

    suspend fun search(data: String): List<Note>? =
        withContext(Dispatchers.IO) {
            return@withContext database.getDao().search(data)
        }


    suspend fun delete(note: Note) {
        withContext(Dispatchers.IO) {
            database.getDao().delete(note.date)
        }
    }
}