package com.example.pocketpal.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface DatabaseDao {
    @Upsert
    suspend fun insertUserDetails(userDetails: UserDetails)

    @Upsert
    suspend fun insertExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT COUNT(*) FROM USERDETAILS")
    suspend fun checkIfUserExists(): Int

    @Query("SELECT * FROM userDetails LIMIT 1")
    fun getUserDetails(): LiveData<UserDetails>

    @Query("SELECT * FROM expense WHERE type = 'Expense' ORDER BY id DESC")
    fun getExpenseDetails(): LiveData<List<Expense>>

    @Query("SELECT SUM(amount) FROM expense WHERE type IN ('Expense', 'Transfer')")
    fun getTotalExpense(): LiveData<Long>

    @Query("SELECT monthlyBudget FROM userDetails WHERE id=1")
    suspend fun getMonthlyBudget(): Int

    @Query("SELECT " +
            "COALESCE((SELECT SUM(amount) FROM expense WHERE type = 'Income'), 0)" +
            " + (SELECT income FROM userDetails WHERE id = 1) As totalIncome")
    fun getTotalIncome() : LiveData<Int>
}