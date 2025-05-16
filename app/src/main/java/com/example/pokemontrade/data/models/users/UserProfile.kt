package com.example.pokemontrade.data.models.users

data class UserProfile(
    val id: Int,
    val name: String,
    val email: String,
    val username: String,
    val location: String,
    val rating: Float? = null,
    val reviewsCount: Int = 0
)
