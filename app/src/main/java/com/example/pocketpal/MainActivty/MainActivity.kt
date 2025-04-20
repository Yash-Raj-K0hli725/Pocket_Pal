package com.example.pocketpal.MainActivty

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.pocketpal.R
import com.example.pocketpal.database.Expense
import com.example.pocketpal.database.ExpenseDatabase
import com.example.pocketpal.database.ExpenseRepository
import com.example.pocketpal.database.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val navBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.updatePadding(bottom = navBars.bottom)
            insets
        }

        val expenseDatabase = ExpenseDatabase.getDatabase(applicationContext)
        val expenseRepository = ExpenseRepository(expenseDatabase)
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(expenseRepository))[MainViewModel::class.java]

        lifecycleScope.launch(Dispatchers.IO) {
            expenseDatabase
                .databaseDao()
                .insertExpense(
                    Expense(300, "pleasure", 1744848000000, "cash", "sasti rand ki pilai", 1)
                )
        }

//        deleteDatabase("expenseDB")
    }
}