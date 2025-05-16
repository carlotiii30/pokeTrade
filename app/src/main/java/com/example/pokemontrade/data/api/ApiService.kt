package com.example.pokemontrade.data.api

import com.example.pokemontrade.data.models.auth.AuthResponse
import com.example.pokemontrade.data.models.auth.LoginRequest
import com.example.pokemontrade.data.models.auth.RegisterRequest
import com.example.pokemontrade.data.models.cards.CardCreate
import com.example.pokemontrade.data.models.cards.CardResponse
import com.example.pokemontrade.data.models.users.UserProfile
import com.example.pokemontrade.data.models.users.UserProfileRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login/")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/register/")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET("users/profile")
    suspend fun getUserProfile(): UserProfile

    @PATCH("users/profile")
    suspend fun updateUserProfile(
        @Body user: UserProfileRequest
    ): UserProfile

    @GET("cards/")
    suspend fun getMyCards(): List<CardResponse>

    @POST("cards/")
    suspend fun createCard(
        @Body card: CardCreate
    ): CardResponse

    @DELETE("cards/{id}/")
    suspend fun deleteCard(
        @Path("id") cardId: Int,
    )

    @GET("cards/all/")
    suspend fun getAllCards(): List<CardResponse>
}
