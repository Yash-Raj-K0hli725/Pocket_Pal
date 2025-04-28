package com.example.pocketpal.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "expense")
data class Expense(
    val type : Int,
    val amount: Int,
    val category: String,
    val date: LocalDate,
    val paymentMode: Int,
    val note: String ?= null,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)