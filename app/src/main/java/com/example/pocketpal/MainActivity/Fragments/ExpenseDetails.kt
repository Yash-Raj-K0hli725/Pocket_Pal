package com.example.pocketpal.MainActivity.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
    private var category: String = ""
    private var type: String = "Expense"

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
        val accountImageResource = requireContext().resources.getIdentifier(
            "wallet",
            "drawable",
            requireContext().packageName
        )
        val categoryImageResource = requireContext().resources.getIdentifier(
            "category",
            "drawable",
            requireContext().packageName
        )
        bind.mtgSelectType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btn_Income -> {
                        type = "Income"
                        bind.apply {
                            llAccount.isClickable = false
                            llCategory.isClickable = false
                            ivSelectAccount.setImageResource(accountImageResource)
                            ivSelectCategory.setImageResource(categoryImageResource)
                            txtViewAccount.text = "Account"
                            txtViewCategory.text = "Category"
                            tvType.text = "Type"
                            tvCategory.text = "Category"
                            llCategory.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
                            llAccount.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
                        }
                        bind.llSave.setOnClickListener {
                            if (bind.tfExpense.text.toString().isNotEmpty()) {
                                lifecycleScope.launch(Dispatchers.IO) {
                                    saveExpenseDetails()
                                    withContext(Dispatchers.Main) {
                                        findNavController().popBackStack()
                                    }
                                }
                            }
                        }
                    }

                    R.id.btn_Expense -> {
                        type = "Expense"
                        bind.apply {
                            llAccount.isClickable = true
                            llCategory.isClickable = true
                            ivSelectCategory.setImageResource(categoryImageResource)
                            txtViewCategory.text = "Category"
                            tvType.text = "Type"
                            tvCategory.text = "Category"
                            llCategory.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.peach))
                            llAccount.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.peach))
                        }
                    }

                    R.id.btn_Transfer -> {
                        type = "Transfer"
                        bind.apply {
                            llAccount.isClickable = false
                            llCategory.isClickable = false
                            ivSelectAccount.setImageResource(accountImageResource)
                            ivSelectCategory.setImageResource(accountImageResource)
                            txtViewAccount.text = "Account"
                            txtViewCategory.text = "Account"
                            tvType.text = "From"
                            tvCategory.text = "To"
                            llCategory.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
                            llAccount.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
                        }
                        bind.llSave.setOnClickListener {
                            if (bind.tfExpense.text.toString().isNotEmpty()) {
                                lifecycleScope.launch(Dispatchers.IO) {
                                    saveExpenseDetails()
                                    withContext(Dispatchers.Main) {
                                        findNavController().popBackStack()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
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
        bind.llCategory.setOnClickListener {
            findNavController().navigate(R.id.action_expenseDetails_to_selectCategoryDialog)
        }
        getAccountType()
        getCategory()
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
                        bind.txtViewAccount.text = "CARD"
                        requireContext().resources.getIdentifier(
                            "card",
                            "drawable",
                            requireContext().packageName
                        )
                    }

                    2 -> {
                        bind.txtViewAccount.text = "CASH"
                        requireContext().resources.getIdentifier(
                            "cash",
                            "drawable",
                            requireContext().packageName
                        )
                    }

                    3 -> {
                        bind.txtViewAccount.text = "UPI"
                        requireContext().resources.getIdentifier(
                            "qr_code",
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

    private fun getCategory() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.expenseDetails)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains("category")
            ) {
                category = navBackStackEntry.savedStateHandle["category"]!!
                val imageName = category.lowercase()
                val imageResource = requireContext().resources.getIdentifier(
                    "$imageName",
                    "drawable",
                    requireContext().packageName
                )

                bind.ivSelectCategory.setImageResource(imageResource)

                bind.txtViewCategory.text = category
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
            val amount = tfExpense.text.toString().toInt()
            val category = category
            val date = LocalDate.now()
            val paymentMode = accountType
            val note = tfAddNotes.text.toString()

            mainViewModel.insertExpense(type, amount, category, date, paymentMode, note)
        }
    }

    private fun checkInputFields(): Boolean {
        return bind.tfExpense.text.toString()
            .isNotEmpty() && accountType != 0 && category.isNotEmpty() && type.isNotEmpty()
    }
}