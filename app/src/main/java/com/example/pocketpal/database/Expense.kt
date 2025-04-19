package com.example.pocketpal.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense")
data class Expense(
    val amount: Int,
    val category: String,
    val date: Long,
    val paymentMode: String,
    val note: String ?= null,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)