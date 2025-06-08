package com.example.pokemontrade.data.models.users

import com.google.gson.annotations.SerializedName

data class UserProfile(
    val id: Int,
    val name: String,
    val email: String,
    val username: String,
    val location: String,
    val rating: Float? = null,
    @SerializedName("reviews_count") val reviewsCount: Int = 0,
    @SerializedName("profilePictureUrl") val profilePictureUrl: String?
)