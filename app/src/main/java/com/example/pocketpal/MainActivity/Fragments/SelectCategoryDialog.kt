package com.example.pocketpal.MainActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import com.example.pocketpal.R
import com.example.pocketpal.databinding.FragmentSelectCategoryDialogBinding


class SelectCategoryDialog : DialogFragment() {
    private lateinit var bind: FragmentSelectCategoryDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_select_category_dialog, container, false)

        setButtonClickListener()

        return bind.root
    }

    private fun setButtonClickListener() {
        val btns = listOf(
            bind.ivBaby,
            bind.ivBeauty,
            bind.ivBills,
            bind.ivCar,
            bind.ivClothing,
            bind.ivEducation,
            bind.ivElectronics,
            bind.ivEntertainment,
            bind.ivFood,
            bind.ivHealth,
            bind.ivHome,
            bind.ivInsurance,
            bind.ivShopping,
            bind.ivSocial,
            bind.ivSport,
            bind.ivTax,
            bind.ivTelephone,
            bind.ivTransportation
        )

        for (btn in btns) {
            btn.setOnClickListener {
                when (it.id) {
                    R.id.iv_baby -> returnCategory("Baby")
                    R.id.iv_beauty -> returnCategory("Beauty")
                    R.id.iv_bills -> returnCategory("Bills")
                    R.id.iv_car -> returnCategory("Car")
                    R.id.iv_clothing -> returnCategory("Clothing")
                    R.id.iv_education -> returnCategory("Education")
                    R.id.iv_electronics -> returnCategory("Electronics")
                    R.id.iv_entertainment -> returnCategory("Entertainment")
                    R.id.iv_food -> returnCategory("Food")
                    R.id.iv_health -> returnCategory("Health")
                    R.id.iv_home -> returnCategory("Home")
                    R.id.iv_insurance -> returnCategory("Insurance")
                    R.id.iv_shopping -> returnCategory("Shopping")
                    R.id.iv_social -> returnCategory("Social")
                    R.id.iv_sport -> returnCategory("Sport")
                    R.id.iv_tax -> returnCategory("Tax")
                    R.id.iv_telephone -> returnCategory("Telephone")
                    R.id.iv_transportation -> returnCategory("Transportation")
                }
            }
        }
    }

    private fun returnCategory(category: String) {
        val navHost = requireActivity().findNavController(R.id.mainHost)
        navHost.previousBackStackEntry?.savedStateHandle?.set("category", category)
        dismiss()
    }
}