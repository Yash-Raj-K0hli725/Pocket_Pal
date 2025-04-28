package com.example.pocketpal.MainActivity.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pocketpal.Configuration
import com.example.pocketpal.MainActivity.MainViewModel
import com.example.pocketpal.MainActivity.MainViewModelFactory
import com.example.pocketpal.R
import com.example.pocketpal.database.ExpenseDatabase
import com.example.pocketpal.database.ExpenseRepository
import com.example.pocketpal.databinding.FragmentExpenseDetailsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class ExpenseDetails : Fragment() {
    private lateinit var bind: FragmentExpenseDetailsBinding
    private lateinit var mainViewModel: MainViewModel
    private var accountType: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_expense_details, container, false)

        val database = ExpenseDatabase.getDatabase(requireActivity())
        val expenseRepository = ExpenseRepository(database)
        mainViewModel =
            ViewModelProvider(requireActivity(), MainViewModelFactory(expenseRepository))[MainViewModel::class.java]

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.llSave.setOnClickListener {
            if (checkInputFields()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    saveExpenseDetails()
                    withContext(Dispatchers.Main) {
                        findNavController().popBackStack()
                    }
                }
            }
        }

        bind.llCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        bind.llAccount.setOnClickListener {
            findNavController().navigate(R.id.action_expenseDetails_to_selectAccountDialog)
        }
        getAccountType()
    }

    private fun getAccountType() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.expenseDetails)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains("accountType")
            ) {
                accountType = navBackStackEntry.savedStateHandle["accountType"]!!
                val imageResource = when (accountType) {
                    1 -> {
                        requireContext().resources.getIdentifier(
                            "male_adult",
                            "drawable",
                            requireContext().packageName
                        )
                    }

                    2 -> {
                        requireContext().resources.getIdentifier(
                            "male_kid",
                            "drawable",
                            requireContext().packageName
                        )
                    }

                    3 -> {
                        requireContext().resources.getIdentifier(
                            "female_adult",
                            "drawable",
                            requireContext().packageName
                        )
                    }

                    else -> {
                        0
                    }
                }
                bind.ivSelectAccount.setImageResource(imageResource)
            }
        }

        navBackStackEntry.lifecycle.addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }

        })
    }

    private fun saveExpenseDetails() {
        bind.apply {
            val type = accountType
            val amount = tfExpense.text.toString().toInt()
            val category = "Clothing"
            val date = LocalDate.now()
            val paymentMode = Configuration.UPI
            val note = tfAddNotes.text.toString()
            Log.d("charu", accountType.toString())

            mainViewModel.insertExpense(type, amount, category, date, paymentMode, note)
        }
    }

    private fun checkInputFields(): Boolean {
        return bind.tfExpense.text.toString().isNotEmpty() && accountType != 0
    }
}