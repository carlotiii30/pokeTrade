package com.example.pokemontrade.data.models.trades

import com.google.gson.annotations.SerializedName

data class TradeResponse(
    val id: Int,
    @SerializedName("offeredCardId") val offeredCardId: Int,
    @SerializedName("requestedCardId") val requestedCardId: Int,
    @SerializedName("requesterId") val requesterId: Int,
    @SerializedName("receiverId") val receiverId: Int,
    val statusRequester: String,
    val statusReceiver: String,
    @SerializedName("createdAt") val createdAt: String
)

data class TradeCreate(
    @SerializedName("offered_card_id") val offeredCardId: Int,
    @SerializedName("requested_card_id") val requestedCardId: Int
)
