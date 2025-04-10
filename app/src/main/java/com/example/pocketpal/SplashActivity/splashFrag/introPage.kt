package com.example.pocketpal.SplashActivity.splashFrag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.pocketpal.R
import com.example.pocketpal.SplashActivity.splashFrag.introVPFrags.PageOne
import com.example.pocketpal.SplashActivity.splashFrag.introVPFrags.PageTwo
import com.example.pocketpal.SplashActivity.splashFrag.introVPFrags.introViewPagerAdapter
import com.example.pocketpal.databinding.FragmentIntroPageBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

class introPage : Fragment() {
    private lateinit var bind: FragmentIntroPageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_intro_page, container, false)
        bind.vpIntro.adapter = introViewPagerAdapter(this, listOf(PageOne(), PageTwo()))
        TabLayoutMediator(bind.tabLay, bind.vpIntro) { _, _ ->
        }.attach()

        bind.tabLay.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> bind.card.animate()
                        .translationY(0f)
                        .translationX(0f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .rotation(-90f)
                        .setDuration(400)
                        .start()

                    1 -> bind.card.animate()
                        .translationY(-200f)
                        .translationX(300f)
                        .rotation(0f)
                        .scaleX(1.5f)
                        .scaleY(1.5f)
                        .setDuration(400)
                        .start()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        // Inflate the layout for this fragment
        return bind.root
    }

}