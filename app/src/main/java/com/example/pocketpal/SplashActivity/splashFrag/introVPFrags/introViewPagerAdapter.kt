package com.example.pocketpal.SplashActivity.splashFrag.introVPFrags

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class introViewPagerAdapter(context:Fragment,private val introVPFrags:List<Fragment>):FragmentStateAdapter(context) {
    override fun getItemCount(): Int {
        return introVPFrags.size
    }

    override fun createFragment(position: Int): Fragment {
        return introVPFrags[position]
    }
}