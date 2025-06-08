package com.example.pokemontrade.data.models.cards

import com.google.gson.annotations.SerializedName

data class CardCreate(
    val name: String,
    val description: String?,
    val type: String,
    @SerializedName("imageUrl") val imageUrl: String?,
)
