package com.example.indekos.ui.detailHistory

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.indekos.repository.UserRepository

class DetailHistoryViewModel(application: Application): ViewModel() {
    private val userRepository = UserRepository(application)

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
        userRepository.updateIndekos(
            indekosId,
            userId,
            namaIndekos,
            harga,
            jumlah_bedroom,
            jumlah_cupboard,
            jumlah_kitchen,
            latitde_indekos,
            longitude_indekos,
            alamat,
            kota,
            provinsi,
            photoUrl,
            photoBannerUrl
        )
    }

    fun getIndekosById(indekosId: Int) = userRepository.getIndekosById(indekosId)

    fun deleteIndekos(indekosId: Int) {
        userRepository.deleteIndekos(indekosId)
    }
}