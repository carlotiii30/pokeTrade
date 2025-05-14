package com.example.pokemontrade.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemontrade.data.api.ApiService
import com.example.pokemontrade.data.models.cards.CardResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val api: ApiService) : ViewModel() {

    private val _cards = MutableStateFlow<List<CardResponse>>(emptyList())
    val cards: StateFlow<List<CardResponse>> = _cards

    fun loadAllCards() {
        viewModelScope.launch {
            try {
                val result = api.getAllCards()
                _cards.value = result
            } catch (e: Exception) {
                println("❌ Error al cargar todas las cartas: ${e.message}")
            }
        }
    }
}
