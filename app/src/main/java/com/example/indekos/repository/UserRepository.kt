package com.example.indekos.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.indekos.database.IndekosDao
import com.example.indekos.database.MyAppRoomDatabase
import com.example.indekos.database.UserDao
import com.example.indekos.model.Indekos
import com.example.indekos.model.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
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

    fun getUserById(userId: Int): LiveData<Users> {
        return _UserDao.getUserById(userId)
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
        alamat: String? = null,
        kota: String? = null,
        provinsi: String? = null,
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
                    alamat = alamat,
                    kota = kota,
                    provinsi = provinsi,
                    photoUrl = photoUrl,
                    photoBannerUrl = photoBannerUrl
                )
            )
        }
    }

    fun getAllIndekos(): Flow<List<Indekos>> = _IndekosDao.getAllIndekos()

    fun getIndekosById(indekosId: Int): LiveData<Indekos> = _IndekosDao.getIndekosById(indekosId)

    fun getIndekosByUserId(userId: Int): Flow<List<Indekos>> = _IndekosDao.getIndekosByUserId(userId)

    fun searchIndekos(query: String?): Flow<List<Indekos>> = _IndekosDao.searchIndekos(query)

    fun updateIndekos(
        indekosId: Int,
        userId: Int,
        namaIndekos: String,
        harga: String,
        jumlah_bedroom: String? = null,
        jumlah_cupboard: String? = null,
        jumlah_kitchen: String? = null,
        latitde_indekos: Double,
        longitude_indekos: Double,
        alamat: String? = null,
        kota: String? = null,
        provinsi: String? = null,
        photoUrl: List<String>? = null,
        photoBannerUrl: String? = null
    ) {
        executorService.execute {
            _IndekosDao.updateIndekos(
                Indekos(
                    indekosId = indekosId,
                    userId = userId,
                    name_indekos = namaIndekos,
                    harga = harga,
                    jumlah_bedroom = jumlah_bedroom,
                    jumlah_cupboard = jumlah_cupboard,
                    jumlah_kitchen = jumlah_kitchen,
                    latitude_indekos = latitde_indekos,
                    longitude_indekos = longitude_indekos,
                    alamat = alamat,
                    kota = kota,
                    provinsi = provinsi,
                    photoUrl = photoUrl,
                    photoBannerUrl = photoBannerUrl
                )
            )
        }
    }

    suspend fun deleteIndekos(indekos: Indekos) {
        withContext(Dispatchers.IO) {
            _IndekosDao.deleteIndekos(indekos)
        }
    }
}
