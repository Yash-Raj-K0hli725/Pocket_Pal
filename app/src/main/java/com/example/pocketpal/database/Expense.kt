package com.example.pocketpal.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "expense")
data class Expense(
    val type : String,
    val amount: Int,
    val category: String ?= null,
    val date: LocalDate,
    val paymentMode: Int ?= null,
    val note: String ?= null,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)