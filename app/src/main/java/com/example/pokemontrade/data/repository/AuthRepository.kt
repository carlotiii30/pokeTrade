package com.example.pokemontrade.data.repository

import com.example.pokemontrade.data.RetrofitInstance
import com.example.pokemontrade.data.models.RegisterRequest
import com.example.pokemontrade.data.models.RegisterResponse
import retrofit2.Response

class AuthRepository {
    suspend fun registerUser(request: RegisterRequest): Response<RegisterResponse> {
        return RetrofitInstance.api.registerUser(request)
    }
}
