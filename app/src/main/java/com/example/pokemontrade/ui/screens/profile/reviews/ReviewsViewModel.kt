package com.example.pokemontrade.ui.screens.profile.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemontrade.data.api.RetrofitInstance
import com.example.pokemontrade.data.models.reviews.ReviewResponse
import com.example.pokemontrade.data.storage.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class ReviewWithAuthor(
    val review: ReviewResponse,
    val authorName: String
)

class ReviewsViewModel(private val tokenManager: TokenManager) : ViewModel() {
    private val _reviews = MutableStateFlow<List<ReviewWithAuthor>>(emptyList())
    val reviews: StateFlow<List<ReviewWithAuthor>> = _reviews

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadMyReviews() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = tokenManager.getUserProfileFlow().firstOrNull()
                user?.let {
                    val api = RetrofitInstance.getAuthenticatedApi(tokenManager)
                    val rawReviews = api.getReviewsByUser(it.id)

                    val reviewsWithAuthors = rawReviews.map { review ->
                        val author = api.getUserById(review.authorId)
                        ReviewWithAuthor(review, author.name)
                    }

                    _reviews.value = reviewsWithAuthors
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}