package com.example.pokemontrade.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemontrade.data.api.ApiService
import com.example.pokemontrade.data.models.auth.AuthResponse
import com.example.pokemontrade.data.models.auth.LoginRequest
import com.example.pokemontrade.data.models.auth.RegisterRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AuthViewModel(private val api: ApiService) : ViewModel() {

    fun login(
        email: String,
        password: String,
        onSuccess: (AuthResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = api.login(LoginRequest(email, password))
                onSuccess(response)
            } catch (e: HttpException) {
                onError("Credenciales inválidas")
            } catch (e: IOException) {
                onError("Error de conexión")
            } catch (e: Exception) {
                onError("Error desconocido")
            }
        }
    }

    fun register(
        name: String,
        username: String,
        email: String,
        password: String,
        onSuccess: (AuthResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = RegisterRequest(
                    name = name,
                    email = email,
                    username = username,
                    password = password
                )
                val response = api.register(request)
                onSuccess(response)
            } catch (e: HttpException) {
                onError("Registro inválido")
            } catch (e: IOException) {
                onError("Error de conexión")
            } catch (e: Exception) {
                onError("Error desconocido")
            }
        }
    }
}
