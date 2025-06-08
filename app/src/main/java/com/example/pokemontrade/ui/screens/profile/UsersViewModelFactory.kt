package com.example.pokemontrade.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokemontrade.data.api.RetrofitInstance
import com.example.pokemontrade.data.storage.TokenManager


class UsersViewModelFactory(
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            val api = RetrofitInstance.getAuthenticatedApi(tokenManager)
            return UsersViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
