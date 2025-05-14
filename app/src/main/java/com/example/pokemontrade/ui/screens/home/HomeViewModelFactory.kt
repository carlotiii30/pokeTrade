package com.example.pokemontrade.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.pokemontrade.data.api.ApiService

class HomeViewModelFactory(private val api: ApiService) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(api) as T
    }
}
