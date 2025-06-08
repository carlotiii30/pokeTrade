package com.example.pokemontrade.data.models.auth

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val name: String,
    val email: String,
    val username: String,
    val password: String,
    val location: String = "",
    @SerializedName("profile_picture_url") val profilePictureUrl: String? = null
)
