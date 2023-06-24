package com.example.indekos.ui.login

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.indekos.database.UserDao
import com.example.indekos.database.UsersRoomDatabase
import com.example.indekos.model.Users
import com.example.indekos.repository.UserRepository

class LoginViewModel(application: Application) : ViewModel() {
    private val userRepository = UserRepository(application)

    suspend fun checkCredentials(username: String, password: String): Boolean {
        val user = userRepository.getUserByUsername(username)
        return user != null && user.password == password
    }
}