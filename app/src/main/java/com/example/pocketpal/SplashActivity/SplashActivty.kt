package com.example.pocketpal.SplashActivity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
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
        ViewCompat.setOnApplyWindowInsetsListener(bind.splashMain){v,inset->
            val navBar = inset.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.updatePadding(bottom = navBar.bottom)
            inset
        }
        val dataStore = DataStoreManager(this)
        val repo = UserRepository(dataStore)
        rvm = ViewModelProvider(this, RegisterViewModelFactory(repo))[RegisterViewModel::class.java]

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
    }
}