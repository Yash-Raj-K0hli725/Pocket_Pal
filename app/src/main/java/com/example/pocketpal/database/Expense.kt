package com.example.pocketpal.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "expense")
@Parcelize
data class Expense(
    val type: String,
    val amount: Int,
    val category: String? = null,
    val date: String,
    val paymentMode: Int? = null,
    val note: String? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable 