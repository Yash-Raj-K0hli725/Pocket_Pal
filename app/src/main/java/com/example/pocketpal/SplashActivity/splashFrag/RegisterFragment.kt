package com.example.pocketpal.SplashActivity.splashFrag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.pocketpal.R
import com.example.pocketpal.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    lateinit var bind: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return bind.root
    }
}