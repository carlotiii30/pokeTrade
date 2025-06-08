package com.example.pokemontrade.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.example.pokemontrade.data.api.ApiService
import com.example.pokemontrade.data.models.users.UserProfile
import com.example.pokemontrade.data.models.users.UserProfileRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersViewModel(
    private val apiService: ApiService
) : ViewModel() {

    suspend fun getUserProfile(): UserProfile? = withContext(Dispatchers.IO) {
        return@withContext try {
            apiService.getUserProfile()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun updateUserProfile(updateRequest: UserProfileRequest): UserProfile? = withContext(Dispatchers.IO) {
        return@withContext try {
            apiService.updateUserProfile(updateRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}


