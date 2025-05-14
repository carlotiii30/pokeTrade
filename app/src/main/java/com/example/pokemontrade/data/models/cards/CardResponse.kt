package com.example.pokemontrade.data.models.cards

import com.google.gson.annotations.SerializedName

data class CardResponse(
    val id: Int,
    val name: String,
    val type: String,
    val img: String,
    @SerializedName("userId") val userId: Int
)
