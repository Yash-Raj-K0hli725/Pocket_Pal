package com.example.pocketpal.SplashActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketpal.SplashActivity.data.repository.UserRepository
import com.example.pocketpal.database.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun saveUserData(fullName: String, email: String, password: String, monthlyBudget: Long, income: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.saveUserData(fullName, email, password, monthlyBudget, income)
        }
    }

    fun getUserDetails(): LiveData<UserDetails> {
        return userRepository.getUserDetails()
    }
}