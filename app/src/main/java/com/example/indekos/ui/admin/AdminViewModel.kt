package com.example.indekos.ui.admin

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.indekos.model.Indekos
import com.example.indekos.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel(application: Application) : ViewModel() {
    private val userRepository = UserRepository(application)

    private val _indekosList = MutableLiveData<List<Indekos>>()
    val indekosList: LiveData<List<Indekos>> = _indekosList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun searchIndekos(query: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            userRepository.searchIndekos(query)
                .collect { indekos ->
                    _indekosList.value = indekos
                }
            _isLoading.value = false
        }
    }

    fun getAllIndekos() {
        viewModelScope.launch {
            _isLoading.value = true
            userRepository.getAllIndekos()
                .collect { indekos ->
                    _indekosList.value = indekos
                }
            _isLoading.value = false
        }
    }

    fun deleteIndekos(position: Int) {
        val indekos = _indekosList.value?.get(position)
        indekos?.let {
            viewModelScope.launch {
                userRepository.deleteIndekos(it)
            }
        }
    }
}