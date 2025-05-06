package com.example.pokemontrade.data.api

import com.example.pokemontrade.data.models.auth.AuthResponse
import com.example.pokemontrade.data.models.auth.LoginRequest
import com.example.pokemontrade.data.models.auth.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

}
