package com.example.pocketpal.MainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketpal.database.Expense
import com.example.pocketpal.database.ExpenseRepository
import com.example.pocketpal.database.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

    fun insertExpense(
        type: String,
        amount: Int,
        category: String,
        date: String,
        paymentMode: Int,
        note: String? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.insertExpense(type, amount, category, date, paymentMode, note)
        }
    }

    fun getUserDetails(): LiveData<UserDetails> {
        return expenseRepository.getUserDetails()
    }

    fun totalExpense() = expenseRepository.getTotalExpense()

    suspend fun getMonthlyBudget(): Int {
        return viewModelScope.async(Dispatchers.IO) {
            expenseRepository.getMonthlyBudget()
        }.await()
    }

    val expenseDetails = expenseRepository.getExpenseDetails()

    val getTotalIncome = expenseRepository.getTotalIncome()

    fun deleteExpense(expense: Expense){
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.deleteExpense(expense)
        }
    }

    fun updateExpense(expense: Expense){
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.updateExpense(expense)
        }
    }
}
