package com.example.pokemontrade.ui.screens.profile.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemontrade.data.api.ApiService
import com.example.pokemontrade.data.models.cards.CardCreate
import com.example.pokemontrade.data.models.cards.CardResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardsViewModel(private val api: ApiService) : ViewModel() {

    private val _cards = MutableStateFlow<List<CardResponse>>(emptyList())
    val cards: StateFlow<List<CardResponse>> = _cards

    fun loadMyCards() {
        viewModelScope.launch {
            try {
                val response = api.getMyCards()
                _cards.value = response
            } catch (e: Exception) {
                _cards.value = emptyList()
            }
        }
    }

    fun createCard(
        card: CardCreate, onSuccess: () -> Unit, onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                api.createCard(card)
                onSuccess()
            } catch (e: Exception) {
                onError("No se pudo crear la carta.")
            }
        }
    }

    fun deleteCard(
        cardId: Int, onSuccess: () -> Unit, onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                api.deleteCard(cardId)
                _cards.value = _cards.value.filterNot { it.id == cardId }
                onSuccess()
            } catch (e: Exception) {
                onError("No se pudo eliminar la carta.")
            }
        }
    }

    fun loadUserCards(userId: String) {
        viewModelScope.launch {
            try {
                val response = api.getUserCards(userId)
                _cards.value = response
            } catch (e: Exception) {
                _cards.value = emptyList()
            }
        }
    }

}
