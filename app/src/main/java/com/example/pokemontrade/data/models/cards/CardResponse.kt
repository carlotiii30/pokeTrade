package com.example.pokemontrade.data.models.cards

import com.google.gson.annotations.SerializedName

data class CardResponse(
    val id: Int,
    val name: String,
    val description: String?,
    val type: String,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("userId") val userId: Int
)
