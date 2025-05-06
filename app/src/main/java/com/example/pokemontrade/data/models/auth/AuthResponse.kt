package com.example.pokemontrade.data.models.auth

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
    val access_token: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val username: String,
    val password: String,
    val location: String = ""
)
