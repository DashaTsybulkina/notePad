package com.bignerdranch.android.note.viewModel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.note.repository.FirebaseRepository

class AuthenticationViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository = FirebaseRepository()

    fun registration(email: String, password: String) {
        firebaseRepository.registration(email, password)
    }

    fun signIn(email: String, password: String){
        firebaseRepository.signIn(email, password)
    }

    fun signOut(){
        firebaseRepository.signOut()
    }
}