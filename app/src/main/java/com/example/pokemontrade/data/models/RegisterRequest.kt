package com.example.pokemontrade.data.models

data class RegisterRequest(
    val name: String,
    val email: String,
    val username: String,
    val password: String,
    val location: String = ""
)
