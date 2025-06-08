package com.example.pokemontrade.ui.screens.inbox.TradeDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemontrade.data.api.RetrofitInstance
import com.example.pokemontrade.data.models.trades.TradeResponse
import com.example.pokemontrade.data.storage.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TradeDetailViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _trade = MutableStateFlow<TradeResponse?>(null)
    val trade: StateFlow<TradeResponse?> = _trade

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadTrade(tradeId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val api = RetrofitInstance.getAuthenticatedApi(tokenManager)
                val response = api.getTradeById(tradeId)
                _trade.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun acceptTrade(tradeId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val api = RetrofitInstance.getAuthenticatedApi(tokenManager)
                api.acceptTrade(tradeId)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun rejectTrade(tradeId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val api = RetrofitInstance.getAuthenticatedApi(tokenManager)
                api.declineTrade(tradeId)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
