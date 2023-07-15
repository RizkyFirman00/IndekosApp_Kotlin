package com.example.indekos.ui.history

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

class HistoryViewModel(application: Application) : ViewModel() {
    private val userRepository = UserRepository(application)

    private val _indekosList = MutableLiveData<List<Indekos>>()

    val indekosList: LiveData<List<Indekos>> = _indekosList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getByUserId(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            userRepository.getIndekosByUserId(userId)
                .collect { indekos ->
                    _indekosList.value = indekos
                }
            _isLoading.value = false
        }
    }
}