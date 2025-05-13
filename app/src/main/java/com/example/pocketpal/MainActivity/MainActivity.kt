package com.example.pocketpal.MainActivity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pocketpal.R
import com.example.pocketpal.database.ExpenseDatabase
import com.example.pocketpal.database.ExpenseRepository
import com.example.pocketpal.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var bind: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private var budget: Int = 0

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(bind.bottomNavigationView) { _, insets ->
            val navBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            bind.main.updatePadding(bottom = navBars.bottom)
            insets
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val expenseDatabase = ExpenseDatabase.getDatabase(applicationContext)
        val expenseRepository = ExpenseRepository(expenseDatabase)
        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(expenseRepository)
        )[MainViewModel::class.java]

        val navController = findNavController(R.id.mainHost)
        bind.bottomNavigationView.setupWithNavController(navController)
        val onBackPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!navController.popBackStack())
                    finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressCallback)

        bind.addItem.setOnClickListener {
            it.visibility = View.GONE
            if (bind.bottomNavigationView.selectedItemId == R.id.miHome) {
                navController.navigate(R.id.action_miHome_to_add)
            } else {
                navController.navigate(R.id.action_miRecords_to_add)
            }
        }
        requestNotificationPermission()
        createNotificationChannel()
        sendNotification()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }


    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "notification_channel")
            .setContentTitle("Pocket Pal")
            .setContentText("Your Budget is about to exceed!")
            .setSmallIcon(R.drawable.analysis)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "notification_channel",
            "Budget Notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = ContextCompat.getSystemService(this, NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }

    private fun sendNotification(){
        val prefs = getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        lifecycleScope.launch(Dispatchers.IO) {
            budget = mainViewModel.getMonthlyBudget()
            budget = (budget * 90) / 100
        }

        mainViewModel.totalExpense().observe(this) {
            val hasSent = prefs.getBoolean("has_sent_notification", false)
            if (it != null && it >= budget && !hasSent) {
                val notification = createNotification()
                val notificationManager = NotificationManagerCompat.from(this)
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    notificationManager.notify(0, notification)
                    prefs.edit().putBoolean("has_sent_notification", true).apply()
                }
            }
        }
    }

}