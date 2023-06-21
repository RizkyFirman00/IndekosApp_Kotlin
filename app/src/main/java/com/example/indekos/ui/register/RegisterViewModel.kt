package com.example.indekos.ui.register

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.indekos.repository.UserRepository
import com.example.indekos.util.ViewModelFactory

class RegisterViewModel(application: Application) : ViewModel() {
    private val userRepository = UserRepository(application)

    fun register(email: String, noTelp: String, username: String, password: String) {
        userRepository.registerUser(email, noTelp, username, password)
    }
}