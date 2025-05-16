package com.example.pokemontrade.data.models.auth

import com.google.gson.annotations.SerializedName

data class AuthUser(
    val id: Int,
    val name: String,
    val email: String,
    val username: String,
    val location: String,
    val rating: Float?,
    val reviews_count: Int
)

data class AuthResponse(
    val user: AuthUser,
    @SerializedName("access_token") val accessToken: String
)