package com.example.pokemontrade.ui.screens.home.users.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokemontrade.data.storage.TokenManager

class UserReviewsViewModelFactory(private val tokenManager: TokenManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserReviewsViewModel(tokenManager) as T
    }
}
