package com.example.pocketpal.MainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketpal.database.ExpenseRepository
import com.example.pocketpal.database.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date

class MainViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {
    fun insertExpense(type: String, amount: Int, category: String, date: LocalDate, paymentMode: Int, note: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.insertExpense(type, amount, category, date, paymentMode, note)
        }
    }

    fun getUserDetails(): LiveData<UserDetails> {
        return expenseRepository.getUserDetails()
    }

    fun totalExpense() = expenseRepository.getTotalExpense()
}