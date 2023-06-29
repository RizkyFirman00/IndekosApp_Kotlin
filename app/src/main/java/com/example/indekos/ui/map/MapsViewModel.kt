package com.example.indekos.ui.map

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.indekos.model.Indekos
import com.example.indekos.repository.UserRepository
import kotlinx.coroutines.launch

class MapsViewModel(application: Application): ViewModel() {
    private val userRepository = UserRepository(application)

    private val _indekosList = MutableLiveData<List<Indekos>>()

    val indekosList: LiveData<List<Indekos>> = _indekosList

    fun getAllIndekos() {
        viewModelScope.launch {
            userRepository.getAllIndekos()
                .collect { indekos ->
                    _indekosList.value = indekos
                }
        }
    }
}