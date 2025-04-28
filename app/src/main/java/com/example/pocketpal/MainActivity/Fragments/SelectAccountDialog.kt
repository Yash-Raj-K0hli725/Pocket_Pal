package com.example.pocketpal.MainActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import com.example.pocketpal.Configuration
import com.example.pocketpal.R
import com.example.pocketpal.databinding.FragmentSelectAccountDialogBinding

class SelectAccountDialog : DialogFragment() {
    private lateinit var bind: FragmentSelectAccountDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_select_account_dialog, container, false)

        setButtonClickListener()

        return bind.root
    }

    private fun setButtonClickListener() {
        val btns = listOf(
            bind.ivCard,
            bind.ivCash,
            bind.ivUpi
        )

        for (btn in btns) {
            btn.setOnClickListener {
                when (it.id) {
                    R.id.ivCard -> returnAccountType(Configuration.CARD)
                    R.id.ivCash -> returnAccountType(Configuration.CASH)
                    R.id.ivUpi -> returnAccountType(Configuration.UPI)
                }
            }
        }
    }

    private fun returnAccountType(account: Int) {
        val navHost = requireActivity().findNavController(R.id.mainHost)
        navHost.previousBackStackEntry?.savedStateHandle?.set("accountType", account)
        dismiss()
    }
}