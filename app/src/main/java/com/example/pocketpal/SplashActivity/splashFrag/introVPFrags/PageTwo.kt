package com.example.pocketpal.SplashActivity.splashFrag.introVPFrags

import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.pocketpal.R
import com.example.pocketpal.databinding.FragmentPageTwoBinding


class PageTwo : Fragment() {
    lateinit var bind:FragmentPageTwoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater,R.layout.fragment_page_two,container,false)
        bind.begin.setOnClickListener{
            findNavController().navigate(R.id.action_introPage_to_registerFragment)
        }
        // Inflate the layout for this fragment
        return bind.root
    }
}