package com.example.indekos.ui.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.indekos.repository.UserRepository

class DetailViewModel(application: Application): ViewModel() {
    private val userRepository = UserRepository(application)

    fun getByIndekosId(indekosId: Int) = userRepository.getIndekosById(indekosId)
}