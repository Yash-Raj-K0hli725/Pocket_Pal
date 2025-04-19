package com.example.pocketpal.MainActivty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketpal.database.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {
    fun insertExpense(amount: Int, category: String, date: Long, paymentMode: String, note: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.insertExpense(amount, category, date, paymentMode,  note)
        }
    }
}