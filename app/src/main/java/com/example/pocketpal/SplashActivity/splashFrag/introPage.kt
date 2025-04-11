package com.example.pocketpal.SplashActivity.splashFrag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.example.pocketpal.R
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
        bind.vpIntro.adapter = introViewPagerAdapter(listOf(0, 1))
        TabLayoutMediator(bind.tabLay, bind.vpIntro) { _, _ ->
        }.attach()

        bind.tabLay.addOnTabSelectedListener(object : OnTabSelectedListener {
            val fadeIn = AnimationUtils.loadAnimation(requireContext(),R.anim.fade_in)
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0->{
                        animateCard(0)
                        bind.begin.visibility = View.GONE
                        bind.initialText3.visibility = View.GONE
                        bind.begin.setOnClickListener(null)
                    }
                    1-> {
                        animateCard(1)
                        setNavigation()
                        bind.initialText3.visibility = View.VISIBLE
                        bind.initialText3.startAnimation(fadeIn)
                    }
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

    private fun animateCard(animationFactor: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(bind.introMain)

        if (animationFactor == 0) {
            constraintSet.connect(
                bind.card.id,
                ConstraintSet.BOTTOM,
                bind.initialText1.id,
                ConstraintSet.BOTTOM
            )

            constraintSet.connect(
                bind.card.id,
                ConstraintSet.TOP,
                bind.initialText1.id,
                ConstraintSet.TOP
            )

            constraintSet.connect(
                bind.card.id,
                ConstraintSet.END,
                bind.cardHelper.id,
                ConstraintSet.START
            )

            constraintSet.connect(
                bind.card.id,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START
            )

            bind.card.animate()
                .rotation(-90f)
                .setDuration(600)
                .start()

            bind.initialText1
                .animate()
                .setDuration(400)
                .alpha(1f)
                .start()
            bind.initialText2
                .animate()
                .setDuration(400)
                .alpha(1f)
                .start()
        }
        else{
            constraintSet.connect(
                bind.card.id,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START
            )

            constraintSet.connect(
                bind.card.id,
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END
            )

            constraintSet.connect(
                bind.card.id,
                ConstraintSet.BOTTOM,
                bind.mid.id,
                ConstraintSet.TOP
            )

            constraintSet.connect(
                bind.card.id,
                ConstraintSet.TOP,
                bind.cardTop.id,
                ConstraintSet.BOTTOM
            )

            bind.card.animate()
                .rotation(0f)
                .setDuration(400)
                .start()
            bind.initialText1
                .animate()
                .setDuration(200)
                .alpha(0f)
                .start()
            bind.initialText2
                .animate()
                .setDuration(200)
                .alpha(0f)
                .start()
        }
        TransitionManager.beginDelayedTransition(bind.introMain, ChangeBounds().apply {
            duration = 600
            interpolator = DecelerateInterpolator(1.8f)
        })
        constraintSet.applyTo(bind.introMain)

    }

    private fun setNavigation() {
            bind.begin.visibility = View.VISIBLE
            bind.begin.setOnClickListener {
                findNavController().navigate(R.id.action_introPage_to_registerFragment)
            }
    }

    override fun onResume() {
        bind.vpIntro.currentItem = 0
        super.onResume()
    }
}

