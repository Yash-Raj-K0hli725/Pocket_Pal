package com.example.pocketpal.SplashActivity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.pocketpal.R
import com.example.pocketpal.SplashActivity.data.dataStore.DataStoreManager
import com.example.pocketpal.SplashActivity.data.repository.UserRepository
import com.example.pocketpal.databinding.ActivitySplashActivtyBinding


class SplashActivty : AppCompatActivity() {
    private lateinit var bind: ActivitySplashActivtyBinding
    private lateinit var rvm: RegisterViewModel

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = DataBindingUtil.setContentView(this, R.layout.activity_splash_activty)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val backPressHandling = object : OnBackPressedCallback(true) {
            val navController = findNavController(R.id.splashHost)
            override fun handleOnBackPressed() {
                if (!navController.popBackStack()) {
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, backPressHandling)

        val dataStoreManager = DataStoreManager(applicationContext)
        val userRepository = UserRepository(dataStoreManager)
        rvm = ViewModelProvider(this, RegisterViewModelFactory(userRepository))[RegisterViewModel::class.java]
    }
}