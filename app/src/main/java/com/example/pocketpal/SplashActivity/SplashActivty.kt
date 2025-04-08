package com.example.pocketpal.SplashActivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.pocketpal.R
import com.example.pocketpal.databinding.ActivitySplashActivtyBinding

class SplashActivty : AppCompatActivity() {
    private lateinit var bind:ActivitySplashActivtyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = DataBindingUtil.setContentView(this,R.layout.activity_splash_activty)



    }

}