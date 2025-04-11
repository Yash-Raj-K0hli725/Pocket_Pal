package com.example.pocketpal.SplashActivity.splashFrag.introVPFrags

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketpal.R

class introViewPagerAdapter(val items: List<Int>) :
    RecyclerView.Adapter<introViewPagerAdapter.ViewPagerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        return ViewPagerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.intro_vpager, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewPagerViewHolder(item: View) : RecyclerView.ViewHolder(item)
}