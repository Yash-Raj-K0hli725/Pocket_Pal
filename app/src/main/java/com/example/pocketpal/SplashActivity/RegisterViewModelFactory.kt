package com.example.pocketpal.SplashActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pocketpal.SplashActivity.data.repository.UserRepository

class RegisterViewModelFactory(private val repo : UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterViewModel(repo) as T
    }
}