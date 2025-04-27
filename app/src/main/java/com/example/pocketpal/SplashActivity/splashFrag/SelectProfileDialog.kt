package com.example.pocketpal.SplashActivity.splashFrag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import com.example.pocketpal.R
import com.example.pocketpal.databinding.FragmentSelectProfileDialogBinding


class SelectProfileDialog : DialogFragment() {
    lateinit var bind: FragmentSelectProfileDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        bind = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_select_profile_dialog,
            container,
            false
        )
        setButtonClickListner()
        // Inflate the layout for this fragment
        return bind.root
    }

    private fun setButtonClickListner() {
        val btns = listOf(
            bind.maleA,
            bind.maleK,
            bind.femaleA,
            bind.femaleK
        )

        for (i in btns) {
            i.setOnClickListener { v ->
                when (v.id) {
                    R.id.maleA -> returnProfileName("male_adult")
                    R.id.maleK -> returnProfileName("male_kid")
                    R.id.femaleA -> returnProfileName("female_adult")
                    R.id.femaleK -> returnProfileName("female_kid")
                }
            }
        }
    }

    private fun returnProfileName(name: String) {
        val navHost = requireActivity().findNavController(R.id.splashHost)
        navHost.previousBackStackEntry?.savedStateHandle?.set("pfpName",name)
        dismiss()
    }
}