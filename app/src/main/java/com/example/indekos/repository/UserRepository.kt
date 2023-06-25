package com.example.indekos.repository

import android.app.Application
import com.example.indekos.database.IndekosDao
import com.example.indekos.database.MyAppRoomDatabase
import com.example.indekos.database.UserDao
import com.example.indekos.model.Indekos
import com.example.indekos.model.Users
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val _UserDao: UserDao
    private val _IndekosDao: IndekosDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = MyAppRoomDatabase.getDatabase(application)
        _UserDao = db.userDao()
        _IndekosDao = db.indekosDao()
    }

    fun registerUser(email: String, noTelp: String, username: String, password: String) {
        executorService.execute {
            _UserDao.registerUser(
                Users(
                    email = email,
                    noTelp = noTelp,
                    username = username,
                    password = password
                )
            )
        }
    }

    suspend fun getUserByUsername(username: String): Users? {
        return _UserDao.getUserByUsername(username)
    }

    fun insertIndekos(
        userId: Int,
        namaIndekos: String,
        harga: String,
        jumlah_bedroom: String? = null,
        jumlah_cupboard: String? = null,
        jumlah_kitchen: String? = null,
        latitde_indekos: Double,
        longitude_indekos: Double,
        photoUrl: List<String>? = null,
        photoBannerUrl: String? = null
    ) {
        executorService.execute {
            _IndekosDao.insertIndekos(
                Indekos(
                    userId = userId,
                    name_indekos = namaIndekos,
                    harga = harga,
                    jumlah_bedroom = jumlah_bedroom,
                    jumlah_cupboard = jumlah_cupboard,
                    jumlah_kitchen = jumlah_kitchen,
                    latitude_indekos = latitde_indekos,
                    longitude_indekos = longitude_indekos,
                    photoUrl = photoUrl,
                    photoBannerUrl = photoBannerUrl
                )
            )
        }
    }
}
