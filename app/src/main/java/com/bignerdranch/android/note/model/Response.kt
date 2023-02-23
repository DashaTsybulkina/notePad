package com.bignerdranch.android.note.model

data class Response(
    var notes: List<Note>? = null,
    var exception: Exception? = null
)