package com.example.pocketpal.SplashActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketpal.SplashActivity.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun saveUserData(fullName: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.saveUserData(fullName, email, password)
        }
    }

    suspend fun getUserName() : String{
        return withContext(Dispatchers.IO) {
            userRepository.getUserData().fullName
        }
    }
}