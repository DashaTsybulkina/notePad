package com.bignerdranch.android.note.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "note")
data class Note(
    @ColumnInfo(name = "data")
    var data:String = "",
    @ColumnInfo(name = "date")
    var date:String = "",
    @ColumnInfo(name = "characters")
    var characters:Long = 0):Serializable{
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null
}
