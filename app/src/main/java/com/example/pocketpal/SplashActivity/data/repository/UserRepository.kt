package com.example.pocketpal.SplashActivity.data.repository

import com.example.pocketpal.SplashActivity.data.dataStore.DataStoreManager
import com.example.pocketpal.SplashActivity.data.model.UserData

class UserRepository(private val dataStoreManager: DataStoreManager) {
    suspend fun getUserData(): UserData = dataStoreManager.getUserData()

    suspend fun saveUserData(fullName: String, email: String, password: String) {
        dataStoreManager.saveUserData(fullName, email, password)
    }
}