package com.example.pokemontrade.data.models

data class RegisterResponse(
    val user: UserDto,
    val access_token: String
)

data class UserDto(
    val id: Int,
    val name: String,
    val email: String,
    val username: String,
    val location: String
)
