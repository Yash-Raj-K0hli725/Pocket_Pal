package com.example.pocketpal.MainActivity

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketpal.database.Expense
import com.example.pocketpal.database.ExpenseRepository
import com.example.pocketpal.database.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

    fun insertExpense(
        type: String,
        amount: Long,
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

    private val month = currentMonth()
    fun monthlyTotalExpense() = expenseRepository.getMonthlyExpense(month)

    val monthlyExpense = expenseRepository.getMonthlyTransactions(month)
    val expenseDetails = expenseRepository.getExpenseDetails()

    suspend fun getMonthlyBudget(): Int {
        return viewModelScope.async(Dispatchers.IO) {
            expenseRepository.getMonthlyBudget()
        }.await()
    }

    @SuppressLint("DefaultLocale")
    private fun currentMonth(): String {
        val now = LocalDate.now()
        return String.format("%04d-%02d", now.year, now.monthValue)
    }

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

    private val _barcode = MutableLiveData<String>()
    val barcode: LiveData<String> get() = _barcode

    fun onBarcodeScanned(value: String){
        _barcode.postValue(value)
    }

    val recentTransactions = expenseRepository.getRecentTransactions()
}
