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
import androidx.navigation.fragment.navArgs
import com.example.pocketpal.MainActivity.MainViewModel
import com.example.pocketpal.MainActivity.MainViewModelFactory
import com.example.pocketpal.R
import com.example.pocketpal.database.Expense
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
    private var argsId: Int = 0

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
        val accountImageResource = requireActivity().resources.getIdentifier(
            "wallet",
            "drawable",
            requireActivity().packageName
        )
        val categoryImageResource = requireActivity().resources.getIdentifier(
            "category",
            "drawable",
            requireActivity().packageName
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
                }
            }
        }
        bind.llSave.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                if (argsId != 0 && bind.tfExpense.text.toString().toInt() != 0) {
                    updateExpense()

                    withContext(Dispatchers.Main) {
                        bind.tfExpense.focusable = 0
                        bind.tfExpense.isClickable = false
                        findNavController().popBackStack()
                    }
                } else {
                    if (checkInputFields()) {
                        saveExpenseDetails()

                        withContext(Dispatchers.Main) {
                            bind.tfExpense.focusable = 0
                            bind.tfExpense.isClickable = false
                            findNavController().popBackStack()
                        }
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
        editExpense()
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
                        requireActivity().resources.getIdentifier(
                            "card",
                            "drawable",
                            requireActivity().packageName
                        )
                    }

                    2 -> {
                        bind.txtViewAccount.text = "CASH"
                        requireActivity().resources.getIdentifier(
                            "cash",
                            "drawable",
                            requireActivity().packageName
                        )
                    }

                    3 -> {
                        bind.txtViewAccount.text = "UPI"
                        requireActivity().resources.getIdentifier(
                            "qr_code",
                            "drawable",
                            requireActivity().packageName
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
                val imageResource = requireActivity().resources.getIdentifier(
                    "$imageName",
                    "drawable",
                    requireActivity().packageName
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
            val amount = tfExpense.text.toString().trim().toIntOrNull() ?: 0
            val category = category
            val date = LocalDate.now()
            val dateString = date.toString()
            val paymentMode = accountType
            val note = tfAddNotes.text.toString()

            mainViewModel.insertExpense(type, amount, category, dateString, paymentMode, note)
        }
    }

    private fun checkInputFields(): Boolean {
        return bind.tfExpense.text.toString()
            .isNotEmpty() && accountType != 0 && category.isNotEmpty()
    }

    private fun editExpense() {
        val args = arguments?.getParcelable<Expense>("expenseDetails")
        if (args != null) {
            argsId = args.id
            val argsAmount = args.amount
            val argsCategory = args.category.toString()
            val argsAccountType = args.paymentMode.toString().toInt()
            val argsNote = args.note

            val accountTypeText = when (argsAccountType) {
                1 -> "Card"
                2 -> "Cash"
                3 -> "Upi"
                else -> "Account"
            }
            val accountTypeImageImage = when (accountTypeText) {
                "Card" -> "card"
                "Cash" -> "cash"
                "Upi" -> "qr_code"
                else -> "account"
            }
            val accountTypeResource = requireActivity().resources.getIdentifier(
                "$accountTypeImageImage",
                "drawable",
                requireActivity().packageName
            )
            val categoryImageResource = requireActivity().resources.getIdentifier(
                "${argsCategory.lowercase()}",
                "drawable",
                requireActivity().packageName
            )

            bind.apply {
                txtViewAccount.text = accountTypeText.uppercase()
                ivSelectAccount.setImageResource(accountTypeResource)
                txtViewCategory.text = argsCategory
                ivSelectCategory.setImageResource(categoryImageResource)
                tfExpense.setText("$argsAmount")
                tfAddNotes.setText("$argsNote")
            }
        }
    }

    private fun updateExpense() {
        val args: ExpenseDetailsArgs by navArgs()
        argsId = args.expenseDetails.id
        var argsCategory = args.expenseDetails.category.toString()
        val argsDate = args.expenseDetails.date.toString()
        var argsAccountType = args.expenseDetails.paymentMode ?: 0

        bind.apply {
            val argsAmount = tfExpense.text.toString().toInt()
            if (category.isNotEmpty()) {
                argsCategory = category
            }
            if (accountType != 0) {
                argsAccountType = accountType
            }
            val argsNote = tfAddNotes.text.toString()

            mainViewModel.updateExpense(
                Expense(
                    type,
                    argsAmount,
                    argsCategory,
                    argsDate,
                    argsAccountType,
                    argsNote,
                    argsId
                )
            )
        }
    }
}