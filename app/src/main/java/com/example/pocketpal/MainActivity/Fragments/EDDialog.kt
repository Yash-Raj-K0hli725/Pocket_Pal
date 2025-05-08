package com.example.pocketpal.MainActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pocketpal.MainActivity.MainViewModel
import com.example.pocketpal.MainActivity.MainViewModelFactory
import com.example.pocketpal.R
import com.example.pocketpal.database.ExpenseDatabase
import com.example.pocketpal.database.ExpenseRepository
import com.example.pocketpal.databinding.FragmentEDDialogBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class EDDialog : DialogFragment() {
    private lateinit var bind: FragmentEDDialogBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_e_d_dialog, container, false)

        val database = ExpenseDatabase.getDatabase(requireActivity())
        val expenseRepository = ExpenseRepository(database)
        mainViewModel =
            ViewModelProvider(requireActivity(), MainViewModelFactory(expenseRepository))[MainViewModel::class.java]

        getData()

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.ivDelete.setOnClickListener {
            deleteExpense()
        }
        bind.ivEdit.setOnClickListener {
            requireActivity().findViewById<CardView>(R.id.nu_cvBottomNavigation)?.let {
                it.visibility = View.GONE
            }
            requireActivity().findViewById<FloatingActionButton>(R.id.addItem)?.let {
                it.visibility = View.GONE
            }
            updateExpense()
        }
        bind.ivCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun getData() {
        val args: EDDialogArgs by navArgs()
        bind.apply {
            tvSpentAmount.text = "-₹${args.expenseDetails.amount}"
            val paymentMode = when (args.expenseDetails.paymentMode) {
                1 -> "Card"
                2 -> "Cash"
                3 -> "Upi"
                else -> ""
            }
            tvAccountText.text = paymentMode
            val paymentModeImageName = when (paymentMode) {
                "Card" -> "card"
                "Cash" -> "cash"
                "Upi" -> "qr_code"
                else -> ""
            }
            val paymentModeImageResource = requireContext().resources.getIdentifier(
                "$paymentModeImageName",
                "drawable",
                requireContext().packageName
            )
            ivAccountImage.setImageResource(paymentModeImageResource)

            val categoryImageName = args.expenseDetails.category?.lowercase()
            val categoryImageResource = requireContext().resources.getIdentifier(
                "$categoryImageName",
                "drawable",
                requireContext().packageName
            )
            ivCategoryImage.setImageResource(categoryImageResource)
            tvCategoryText.text = "${args.expenseDetails.category}"
            val notes = args.expenseDetails.note
            if (notes != null) {
                if (notes.isNotEmpty()) {
                    tvNotes.text = "${args.expenseDetails.note}"
                }
            }
        }
    }

    private fun deleteExpense() {
        val args: EDDialogArgs by navArgs()
        mainViewModel.deleteExpense(args.expenseDetails)
        dismiss()
    }

    private fun updateExpense() {
        val args: EDDialogArgs by navArgs()
        findNavController().navigate(
            EDDialogDirections.actionEDDialogToExpenseDetails(args.expenseDetails)
        )
    }
}