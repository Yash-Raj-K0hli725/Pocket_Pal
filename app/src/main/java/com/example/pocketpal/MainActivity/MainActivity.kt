package com.example.pocketpal.MainActivity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.pocketpal.MainActivity.Fragments.Add
import com.example.pocketpal.MainActivity.Fragments.ExpenseDetails
import com.example.pocketpal.MainActivity.Fragments.Home
import com.example.pocketpal.R
import com.example.pocketpal.database.Expense
import com.example.pocketpal.database.ExpenseDatabase
import com.example.pocketpal.database.ExpenseRepository
import com.example.pocketpal.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var bind: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val navBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.updatePadding(bottom = navBars.bottom)
            insets
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

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

        val homeFragment = Home()
        val addFragment = Add()

        setCurrentFragment(homeFragment)
        bind.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.miHome -> setCurrentFragment(homeFragment)
                R.id.miAdd -> setCurrentFragment(addFragment)
                R.id.miProfile -> setCurrentFragment(ExpenseDetails())
            }
            true
        }

//        deleteDatabase("expenseDB")
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}