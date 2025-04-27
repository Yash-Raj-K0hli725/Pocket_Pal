package com.example.pocketpal.MainActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pocketpal.database.ExpenseRepository

class MainViewModelFactory(val expenseRepository: ExpenseRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(expenseRepository) as T
    }
}