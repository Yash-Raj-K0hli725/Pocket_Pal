package com.example.pocketpal.SplashActivity.data.repository

import androidx.lifecycle.LiveData
import com.example.pocketpal.database.DatabaseDao
import com.example.pocketpal.database.UserDetails

class UserRepository(private val databaseDao: DatabaseDao) {
    suspend fun saveUserData(fullName: String, email: String, password: String, monthlyBudget: Long, income: Long) {
        databaseDao.insertUserDetails(UserDetails(fullName, email, password, monthlyBudget, income, 1))
    }

    fun getUserDetails() : LiveData<UserDetails>{
        return databaseDao.getUserDetails()
    }
}