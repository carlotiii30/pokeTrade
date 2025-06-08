package com.example.pokemontrade.ui.screens.home.cards

import androidx.lifecycle.ViewModel
import com.example.pokemontrade.data.api.ApiService
import com.example.pokemontrade.data.models.cards.CardResponse
import com.example.pokemontrade.data.models.users.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CardDetailHomeViewModel(private val apiService: ApiService) : ViewModel() {
    suspend fun getUserById(userId: Int): UserProfile? = withContext(Dispatchers.IO) {
        return@withContext try {
            apiService.getUserById(userId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun registerCardView(cardId: Int) {
        apiService.registerView(cardId)
    }

    suspend fun getSimilarCardsView(cardId: Int): List<CardResponse> {
        return apiService.getSimilarCards(cardId)
    }

}
