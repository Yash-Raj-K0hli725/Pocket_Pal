package com.example.pocketpal.SplashActivity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.example.pocketpal.databinding.FragmentProfileImageBinding

class ProfileImageDialog(
    context: Context,
    private val listener: OnOptionSelectedListener
) : Dialog(context) {
    private lateinit var bind: FragmentProfileImageBinding

    interface OnOptionSelectedListener {
        fun onOptionSelected(selectedOption: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = FragmentProfileImageBinding.inflate(LayoutInflater.from(context))
        setContentView(bind.root)

        setCancelable(false)

        bind.image1.setOnClickListener {
            val imageId = 1
            listener.onOptionSelected(imageId)
        }
        bind.image2.setOnClickListener {
            val imageId = 2
            listener.onOptionSelected(imageId)
        }
        bind.image3.setOnClickListener {
            val imageId = 3
            listener.onOptionSelected(imageId)
        }
        bind.image4.setOnClickListener {
            val imageId = 4
            listener.onOptionSelected(imageId)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        dismiss()
    }
}