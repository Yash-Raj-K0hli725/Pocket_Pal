package com.example.pocketpal.database

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Budget (
    val monthlyBudget: Long,
    val income: Long,
    val imageName:String
): Parcelable