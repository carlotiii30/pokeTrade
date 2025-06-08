package com.example.pokemontrade.data.models.reviews

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    val id: Int,
    @SerializedName("tradeId") val tradeId: Int,
    @SerializedName("authorId") val authorId: Int,
    @SerializedName("recipientId") val recipientId: Int,
    val rating: Int,
    val comment: String,
    @SerializedName("createdAt") val createdAt: String
)

data class ReviewRequest(
    val tradeId: Int,
    val recipientId: Int,
    val rating: Int,
    val comment: String
)