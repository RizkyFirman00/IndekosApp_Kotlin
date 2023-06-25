package com.example.indekos.ui.addData

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.indekos.repository.UserRepository

class AddDataViewModel(application: Application): ViewModel() {
    private val userRepository = UserRepository(application)

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
        userRepository.insertIndekos(
            userId,
            namaIndekos,
            harga,
            jumlah_bedroom,
            jumlah_cupboard,
            jumlah_kitchen,
            latitde_indekos,
            longitude_indekos,
            photoUrl,
            photoBannerUrl
        )
    }

}