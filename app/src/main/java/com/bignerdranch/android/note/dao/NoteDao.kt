package com.bignerdranch.android.note.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bignerdranch.android.note.model.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Query("UPDATE note SET date = :date, characters = :character, data = :data WHERE date = :oldDate")
    fun update (oldDate:String, character: Long, date:String, data:String)

    @Query("DELETE FROM note WHERE date = :deleteDate")
    fun delete(deleteDate: String)

    @Query("SELECT * FROM note")
    fun getAll(): List<Note>

    @Query("SELECT * FROM note WHERE data LIKE :data")
    fun search(data:String):List<Note>

    @Query("DELETE FROM note")
    fun deleteAll()
}