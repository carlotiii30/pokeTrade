package com.example.pokemontrade.ui.screens.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemontrade.data.api.RetrofitInstance
import com.example.pokemontrade.data.models.trades.TradeResponse
import com.example.pokemontrade.data.storage.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InboxViewModel(private val tokenManager: TokenManager) : ViewModel() {
    private val _trades = MutableStateFlow<List<TradeResponse>>(emptyList())
    val trades: StateFlow<List<TradeResponse>> = _trades

    fun loadTrades() {
        viewModelScope.launch {
            try {
                val api = RetrofitInstance.getAuthenticatedApi(tokenManager)
                _trades.value = api.getMyTrades()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}