package com.example.pocketpal.SplashActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketpal.SplashActivity.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun saveUserData(
        fullName: String,
        email: String,
        password: String,
        monthlyBudget: Long,
        income: Long,
        imageName: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.saveUserData(fullName, email, password, monthlyBudget, income, imageName)
        }
    }

    suspend fun checkIfUserExists(): Boolean {
        return viewModelScope.async {
            userRepository.checkIfUserExists() > 0
        }.await()
    }
}