package com.example.indekos.repository

import android.app.Application
import com.example.indekos.database.UserDao
import com.example.indekos.database.UsersRoomDatabase
import com.example.indekos.model.Users
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val _UserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UsersRoomDatabase.getDatabase(application)
        _UserDao = db.userDao()
    }

    fun registerUser(email: String, noTelp: String, username: String, password: String) {
        executorService.execute {
            _UserDao.registerUser(Users(email = email, noTelp = noTelp, username = username, password = password))
        }
    }

    fun loginUser(username: String, password: String): Users {
        return _UserDao.loginUser(username, password)
    }

    suspend fun getUserByUsername(username: String): Users? {
        return _UserDao.getUserByUsername(username)
    }
}

