package com.example.pokemontrade.data.network

interface TokenProvider {
    suspend fun getToken(): String?
}
