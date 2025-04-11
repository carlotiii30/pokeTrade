package com.example.pokemontrade.ui.screens.auth.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.pokemontrade.data.UserPreferences
import com.example.pokemontrade.data.models.RegisterRequest
import com.example.pokemontrade.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val token: String) : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val userPrefs = UserPreferences(application.applicationContext)

    private val _state = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val state: StateFlow<RegisterState> = _state

    fun register(name: String, email: String, username: String, password: String, location: String = "") {
        viewModelScope.launch {
            _state.value = RegisterState.Loading
            try {
                val request = RegisterRequest(name, email, username, password, location)
                val response = repository.registerUser(request)

                if (response.isSuccessful) {
                    response.body()?.let {
                        userPrefs.saveToken(it.access_token)
                        _state.value = RegisterState.Success(it.access_token)
                    } ?: run {
                        _state.value = RegisterState.Error("Respuesta vacía.")
                    }
                } else {
                    val error = response.errorBody()?.string()
                    _state.value = RegisterState.Error(error ?: "Error desconocido.")
                }
            } catch (e: Exception) {
                _state.value = RegisterState.Error("Error de red: ${e.localizedMessage}")
            }
        }
    }
}
