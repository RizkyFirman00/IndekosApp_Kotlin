package com.example.indekos.database

import com.example.indekos.model.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserDaoImpl : UserDao {
    private val users: MutableList<Users> = mutableListOf()
    override fun getAllUsers(): List<Users> {
        TODO("Not yet implemented")
    }

    override fun registerUser(users: Users) {
        TODO("Not yet implemented")
    }

    override fun loginUser(username: String, password: String): Users {
        TODO("Not yet implemented")
    }
    // Assuming you have a database connection or data source

    override suspend fun getUserByUsername(username: String): Users? {
        return withContext(Dispatchers.IO) {
            users.find { it.username == username }
        }
    }
}