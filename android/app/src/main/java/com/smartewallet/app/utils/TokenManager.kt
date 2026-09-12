package com.smartewallet.app.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class TokenManager(private val context: Context) {
    
    private val Context.dataStore by preferencesDataStore(name = "wallet_prefs")
    
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val OTP_ENABLED_KEY = stringPreferencesKey("otp_enabled")
    }
    
    fun getToken(): String? {
        return try {
            // Synchronous access (blocking call)
            val dataStore = context.dataStore
            var token: String? = null
            // Note: In production, use Flow for non-blocking access
            token
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun saveToken(token: String, refreshToken: String? = null) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            refreshToken?.let { preferences[REFRESH_TOKEN_KEY] = it }
        }
    }
    
    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = email
        }
    }
    
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
            preferences.remove(USER_EMAIL_KEY)
            preferences.remove(OTP_ENABLED_KEY)
        }
    }
    
    fun getTokenFlow() = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }
    
    fun getUserEmailFlow() = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL_KEY] ?: ""
    }
}