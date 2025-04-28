package com.example.pocketpal.database

import java.time.LocalDate
import java.util.Date

class ExpenseRepository(private val expenseDatabase: ExpenseDatabase) {
    suspend fun insertExpense(type: Int,amount: Int, category: String, date: LocalDate, paymentMode: Int, note: String? = null) {
        expenseDatabase.databaseDao().insertExpense(Expense(type, amount, category, date, paymentMode, note))
    }

    suspend fun updateExpense(expense: Expense) = expenseDatabase.databaseDao().updateExpense(expense)

    suspend fun deleteExpense(expense: Expense) = expenseDatabase.databaseDao().deleteExpense(expense)

    fun getExpenseDetails() = expenseDatabase.databaseDao().getExpenseDetails()

    fun getUserDetails() = expenseDatabase.databaseDao().getUserDetails()
}