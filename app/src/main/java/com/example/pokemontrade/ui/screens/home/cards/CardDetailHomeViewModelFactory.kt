package com.example.pokemontrade.ui.screens.home.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokemontrade.data.api.RetrofitInstance
import com.example.pokemontrade.data.storage.TokenManager

class CardDetailHomeViewModelFactory(
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardDetailHomeViewModel::class.java)) {
            val api = RetrofitInstance.getAuthenticatedApi(tokenManager)
            return CardDetailHomeViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
