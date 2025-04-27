package com.example.pocketpal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Expense::class, UserDetails::class], version = 1)
@TypeConverters(Converts::class)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun databaseDao(): DatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getDatabase(context: Context): ExpenseDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, ExpenseDatabase::class.java,
                        "ExpenseDB").build()
                }
            }
            return INSTANCE!!
        }
    }
}