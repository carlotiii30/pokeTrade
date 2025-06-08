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

    private val _recommendedCards = MutableStateFlow<List<CardResponse>>(emptyList())
    val recommendedCards: StateFlow<List<CardResponse>> = _recommendedCards

    private val _mostPopularCards = MutableStateFlow<List<CardResponse>>(emptyList())
    val mostPopularCards: StateFlow<List<CardResponse>> = _mostPopularCards

    private val _recommendedLoaded = MutableStateFlow(false)
    val recommendedLoaded: StateFlow<Boolean> = _recommendedLoaded

    private val _popularLoaded = MutableStateFlow(false)
    val popularLoaded: StateFlow<Boolean> = _popularLoaded

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

    fun loadRecommendedCards() {
        viewModelScope.launch {
            try {
                val result = api.getRecommendedCards()
                _recommendedCards.value = result
                _recommendedLoaded.value = true
            } catch (e: Exception) {
                println("❌ Error al cargar cartas recomendadas: ${e.message}")
            }
        }
    }

    fun loadMostPopularCards() {
        viewModelScope.launch {
            try {
                val result = api.getMostPopular()
                _mostPopularCards.value = result
                _popularLoaded.value = true
            } catch (e: Exception) {
                println("❌ Error al cargar cartas más populares: ${e.message}")
            }
        }
    }


}
