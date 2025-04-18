package com.example.pocketpal.SplashActivity.splashFrag.introVPFrags

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketpal.R

class introViewPagerAdapter :
    RecyclerView.Adapter<introViewPagerAdapter.ViewPagerVH>() {
    inner class ViewPagerVH(item: View) : RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerVH {
        return ViewPagerVH(
            LayoutInflater.from(parent.context).inflate(R.layout.blank, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewPagerVH, position: Int) {

    }

    override fun getItemCount(): Int {
        return 2
    }
}