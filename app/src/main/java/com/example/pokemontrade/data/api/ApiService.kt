package com.example.pokemontrade.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.pokemontrade.data.models.RegisterRequest
import com.example.pokemontrade.data.models.RegisterResponse

interface ApiService {
    @POST("/auth/register")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>
}
