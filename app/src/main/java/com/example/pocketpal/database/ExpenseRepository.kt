package com.example.pocketpal.database

import androidx.lifecycle.LiveData
import java.time.LocalDate

class ExpenseRepository(private val expenseDatabase: ExpenseDatabase) {
    suspend fun insertExpense(
        type: String,
        amount: Int,
        category: String,
        date: LocalDate,
        paymentMode: Int,
        note: String? = null
    ) {
        expenseDatabase.databaseDao().insertExpense(Expense(type, amount, category, date, paymentMode, note))
    }

    suspend fun updateExpense(expense: Expense) = expenseDatabase.databaseDao().updateExpense(expense)

    suspend fun deleteExpense(expense: Expense) = expenseDatabase.databaseDao().deleteExpense(expense)

    fun getExpenseDetails() = expenseDatabase.databaseDao().getExpenseDetails()

    fun getUserDetails() = expenseDatabase.databaseDao().getUserDetails()

    fun getTotalExpense(): LiveData<Long> = expenseDatabase.databaseDao().getTotalExpense()

    suspend fun getMonthlyBudget(): Int = expenseDatabase.databaseDao().getMonthlyBudget()

    fun getTotalIncome() : LiveData<Int> = expenseDatabase.databaseDao().getTotalIncome()
}