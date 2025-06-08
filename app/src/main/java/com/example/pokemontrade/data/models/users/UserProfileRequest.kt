package com.example.pokemontrade.data.models.users

import com.google.gson.annotations.SerializedName

data class UserProfileRequest(
    val name: String? = null,
    val email: String? = null,
    val location: String? = null,
    @SerializedName("profilePictureUrl") val profilePictureUrl: String?
)
