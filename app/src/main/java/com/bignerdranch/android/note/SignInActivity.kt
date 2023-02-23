package com.bignerdranch.android.note

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.note.viewModel.AuthenticationViewModel
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {
    lateinit var viewModel:AuthenticationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        viewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
        viewModel.signOut()

        buttonSignIn.setOnClickListener{
            val email = editTextSignInEmail.text.toString()
            val password= editTextSignInPassword.text.toString()
            viewModel.signIn(email, password)
            finish()
        }

        textViewRegistration.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}