package com.example.pocketpal.SplashActivity.data.repository

import com.example.pocketpal.database.DatabaseDao
import com.example.pocketpal.database.UserDetails

class UserRepository(private val databaseDao: DatabaseDao) {
    suspend fun saveUserData(
        fullName: String,
        email: String,
        password: String,
        monthlyBudget: Long,
        income: Long,
        imageName: String,
    ) {
        databaseDao.insertUserDetails(
            UserDetails(
                fullName,
                email,
                password,
                monthlyBudget,
                income,
                imageName,
                1
            )
        )
    }

    suspend fun checkIfUserExists(): Int {
        return databaseDao.checkIfUserExists()
    }
}