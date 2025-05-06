package com.example.pokemontrade.data.models.trades

import com.google.gson.annotations.SerializedName

data class TradeResponse(
    val id: Int,
    @SerializedName("offeredCardId") val offeredCardId: Int,
    @SerializedName("requestedCardId") val requestedCardId: Int,
    @SerializedName("requesterId") val requesterId: Int,
    @SerializedName("receiverId") val receiverId: Int,
    val status: String,
    @SerializedName("createdAt") val createdAt: String
)
