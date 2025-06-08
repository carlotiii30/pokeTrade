package com.example.pokemontrade.data.api

import com.example.pokemontrade.data.models.auth.AuthResponse
import com.example.pokemontrade.data.models.auth.LoginRequest
import com.example.pokemontrade.data.models.auth.RegisterRequest
import com.example.pokemontrade.data.models.cards.CardCreate
import com.example.pokemontrade.data.models.cards.CardResponse
import com.example.pokemontrade.data.models.reviews.ReviewRequest
import com.example.pokemontrade.data.models.reviews.ReviewResponse
import com.example.pokemontrade.data.models.trades.TradeCreate
import com.example.pokemontrade.data.models.trades.TradeResponse
import com.example.pokemontrade.data.models.upload.UploadImageResponse
import com.example.pokemontrade.data.models.users.UserProfile
import com.example.pokemontrade.data.models.users.UserProfileRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login/")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/register/")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET("users/profile")
    suspend fun getUserProfile(): UserProfile

    @PATCH("users/profile")
    suspend fun updateUserProfile(
        @Body user: UserProfileRequest
    ): UserProfile

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): UserProfile

    @GET("cards/")
    suspend fun getMyCards(): List<CardResponse>

    @POST("cards/")
    suspend fun createCard(
        @Body card: CardCreate
    ): CardResponse

    @DELETE("cards/{id}/")
    suspend fun deleteCard(
        @Path("id") cardId: Int,
    )

    @GET("cards/all/")
    suspend fun getAllCards(): List<CardResponse>

    @GET("recommendations/for-you")
    suspend fun getRecommendedCards(): List<CardResponse>

    @GET("recommendations/same-user-cards/{id}")
    suspend fun getSimilarCards(@Path("id") cardId: Int): List<CardResponse>

    @POST("views/{id}")
    suspend fun registerView(@Path("id") cardId: Int)

    @GET("views/popular")
    suspend fun getMostPopular(): List<CardResponse>

    @GET("cards/user/{id}/")
    suspend fun getUserCards(
        @Path("id") userId: String
    ): List<CardResponse>

    @GET("cards/{id}/")
    suspend fun getCardById(@Path("id") cardId: Int): CardResponse

    @POST("reviews/")
    suspend fun leaveReview(@Body review: ReviewRequest)

    @GET("reviews/user/{userId}")
    suspend fun getReviewsByUser(@Path("userId") userId: Int): List<ReviewResponse>

    @GET("trades/")
    suspend fun getMyTrades(): List<TradeResponse>

    @POST("trades/")
    suspend fun createTrade(@Body trade: TradeCreate)

    @GET("trades/{trade_id}")
    suspend fun getTradeById(@Path("trade_id") tradeId: Int): TradeResponse

    @POST("trades/{trade_id}/accept")
    suspend fun acceptTrade(@Path("trade_id") tradeId: Int)

    @POST("trades/{trade_id}/decline")
    suspend fun declineTrade(@Path("trade_id") tradeId: Int)

    @POST("trades/{trade_id}/complete")
    suspend fun completeTrade(@Path("trade_id") tradeId: Int)

    @Multipart
    @POST("upload/image")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): UploadImageResponse

}
