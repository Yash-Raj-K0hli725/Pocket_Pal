package com.example.pocketpal.database

class ExpenseRepository(private val expenseDatabase: ExpenseDatabase) {
    suspend fun insertExpense(amount: Int, category: String, date: Long, paymentMode: String, note: String? = null) {
        expenseDatabase.databaseDao().insertExpense(Expense(amount, category, date, paymentMode, note))
    }

    suspend fun updateExpense(expense: Expense) = expenseDatabase.databaseDao().updateExpense(expense)

    suspend fun deleteExpense(expense: Expense) = expenseDatabase.databaseDao().deleteExpense(expense)

    fun getExpenseDetails() = expenseDatabase.databaseDao().getExpenseDetails()
}
