package com.example.pocketpal.SplashActivity.splashFrag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.pocketpal.R
import com.example.pocketpal.databinding.FragmentIntroPageBinding

class introPage : Fragment() {
    private lateinit var bind :FragmentIntroPageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater,R.layout.fragment_intro_page,container,false)
        // Inflate the layout for this fragment
        return bind.root
    }

}