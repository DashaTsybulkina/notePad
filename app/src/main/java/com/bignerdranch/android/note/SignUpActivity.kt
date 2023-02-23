package com.bignerdranch.android.note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.note.viewModel.AuthenticationViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    lateinit var viewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        viewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)

        buttonRegistration.setOnClickListener{
            val email: String = editTextEmailSignUp.text.toString()
            val password: String = editTextPasswordSignUp.text.toString()
            viewModel.registration(email, password)
            finish()
        }
    }
}