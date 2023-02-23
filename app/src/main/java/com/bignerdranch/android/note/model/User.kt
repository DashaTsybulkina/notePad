package com.bignerdranch.android.note.model

import android.net.Uri

data class User(
    val email: String = "",
    val fullName: String = "",
    val photoURL: Uri? = null
)