package com.example.pocketpal.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userDetails")
data class UserDetails(
    val fullName: String,
    val email: String,
    val password: String,
    val monthlyBudget: Long,
    val income: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)