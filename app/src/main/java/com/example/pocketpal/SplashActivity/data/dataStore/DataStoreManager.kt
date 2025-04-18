package com.example.pocketpal.SplashActivity.data.dataStore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.pocketpal.SplashActivity.data.model.UserData
import kotlinx.coroutines.flow.first

val Context.dataStore by preferencesDataStore("user_prefs")

class DataStoreManager(context: Context) {

    private val dataStore = context.dataStore

    val FULL_NAME = stringPreferencesKey("full_name")
    val EMAIL = stringPreferencesKey("email")
    val PASSWORD = stringPreferencesKey("password")

    suspend fun saveUserData(fullName: String, email: String, password: String) {
        dataStore.edit { prefs ->
            prefs[FULL_NAME] = fullName
            prefs[EMAIL] = email
            prefs[PASSWORD] = password
        }
    }

    suspend fun getUserData(): UserData {
        val prefs = dataStore.data.first() //this gets the data once and stops
        return UserData(
            fullName = prefs[FULL_NAME] ?: "",
            email = prefs[EMAIL] ?: "",
            password = prefs[PASSWORD] ?: ""
        )
    }
}