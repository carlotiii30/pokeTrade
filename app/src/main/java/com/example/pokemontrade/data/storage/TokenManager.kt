package com.example.pokemontrade.data.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.pokemontrade.data.models.users.UserProfile
import com.example.pokemontrade.data.network.TokenProvider
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "user_prefs"
val Context.dataStore by preferencesDataStore(DATASTORE_NAME)

class TokenManager(private val context: Context) : TokenProvider {

    companion object {
        val TOKEN_KEY = stringPreferencesKey("access_token")
        val USER_PROFILE_KEY = stringPreferencesKey("user_profile")
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs -> prefs[TOKEN_KEY] = token }
    }

    fun getTokenFlow(): Flow<String?> {
        return context.dataStore.data.map { prefs -> prefs[TOKEN_KEY] }
    }

    override suspend fun getToken(): String? {
        return context.dataStore.data.firstOrNull()?.get(TOKEN_KEY)
    }

    suspend fun clearToken() {
        context.dataStore.edit { prefs -> prefs.remove(TOKEN_KEY) }
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        val json = Gson().toJson(profile)
        context.dataStore.edit { prefs -> prefs[USER_PROFILE_KEY] = json }
    }

    suspend fun getUserProfile(): UserProfile? {
        val prefs = context.dataStore.data.firstOrNull()
        val json = prefs?.get(USER_PROFILE_KEY)
        return json?.let { Gson().fromJson(it, UserProfile::class.java) }
    }

    fun getUserProfileFlow(): Flow<UserProfile?> {
        return context.dataStore.data.map { prefs ->
            prefs[USER_PROFILE_KEY]?.let { Gson().fromJson(it, UserProfile::class.java) }
        }
    }
}
