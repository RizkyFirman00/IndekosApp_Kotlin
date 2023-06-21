package com.example.indekos.ui.login

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.indekos.repository.UserRepository

class LoginViewModel(application: Application) : ViewModel() {
    private val userRepository = UserRepository(application)

    fun login(username: String, password: String) = userRepository.loginUser(username, password)
}