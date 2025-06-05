package com.example.pocketpal.MainActivity.Fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequestBuilder
import com.example.pocketpal.MainActivity.BudgetNotificationWorker
import com.example.pocketpal.MainActivity.MainViewModel
import com.example.pocketpal.MainActivity.MainViewModelFactory
import com.example.pocketpal.MainActivity.RecordsAdapter
import com.example.pocketpal.R
import com.example.pocketpal.database.ExpenseDatabase
import com.example.pocketpal.database.ExpenseRepository
import com.example.pocketpal.databinding.FragmentRecordsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class Records : Fragment() {
    private lateinit var bind: FragmentRecordsBinding
    private lateinit var mainViewModel: MainViewModel
    private var budget: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_records, container, false)

        val database = ExpenseDatabase.getDatabase(requireActivity())
        val expenseRepository = ExpenseRepository(database)
        mainViewModel =
            ViewModelProvider(requireActivity(), MainViewModelFactory(expenseRepository))[MainViewModel::class.java]

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<CardView>(R.id.nu_cvBottomNavigation)?.let {
            it.visibility = View.VISIBLE
        }
        requireActivity().findViewById<FloatingActionButton>(R.id.addItem)?.let {
            it.visibility = View.VISIBLE
        }
        recordsTopDetails()
        recordsAdapter()

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            sendNotification()
        }
    }

    private fun recordsTopDetails() {
        mainViewModel.monthlyTotalExpense().observe(viewLifecycleOwner) {
            if (it != null) {
                bind.apply {
                    expenseText.text = "-₹${it.toString()}"
                    ivHistory.visibility = View.GONE
                    tvHistory.visibility = View.GONE
                }
            } else {
                bind.apply {
                    expenseText.text = "-₹0"
                    ivHistory.visibility = View.VISIBLE
                    tvHistory.visibility = View.VISIBLE
                }

            }
        }

        mainViewModel.getTotalIncome.observe(viewLifecycleOwner) {
            if (it != null) {
                bind.incomeText.text = "₹${it.toString()}"
            }
        }

        lifecycleScope.launch {
            val monthlyBudget = mainViewModel.getMonthlyBudget()
            bind.budgetText.text = "₹$monthlyBudget"
        }
    }

    private fun recordsAdapter() {
        val navController = requireActivity().findNavController(R.id.mainHost)
        val adapter = RecordsAdapter(requireActivity(), navController) { expense ->
            findNavController().navigate(
                RecordsDirections.actionMiRecordsToEDDialog(expense)
            )
        }
        bind.apply {
            rvExpense.adapter = adapter
            rvExpense.layoutManager = LinearLayoutManager(requireActivity())
        }
        mainViewModel.monthlyExpense.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it)
            }
        }
    }

    private fun sendNotification() {
        val prefs = requireActivity().getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        lifecycleScope.launch(Dispatchers.IO) {
            budget = mainViewModel.getMonthlyBudget()
            val threshold = (budget * 90) / 100

            val aboutToExceedSent = prefs.getBoolean("has_sent_about_to_exceed", false)
            val budgetExceedSent = prefs.getBoolean("has_sent_budget_exceeded", false)
            withContext(Dispatchers.Main) {
                mainViewModel.totalExpense().observe(viewLifecycleOwner) {
                    if (it != null && it >= threshold && !aboutToExceedSent) {

                        val inputData = androidx.work.Data.Builder()
                            .putInt("threshold", threshold)
                            .putInt("budget", budget)
                            .putLong("expense", it)
                            .build()

                        val workRequest = OneTimeWorkRequestBuilder<BudgetNotificationWorker>()
                            .setInputData(inputData)
                            .setInitialDelay(5, TimeUnit.SECONDS)
                            .build()

                        androidx.work.WorkManager.getInstance(requireActivity())
                            .enqueue(workRequest)

                        prefs.edit().putBoolean("has_sent_about_to_exceed", true).apply()
                    } else if (it != null && it >= budget && !budgetExceedSent) {
                        val inputData = androidx.work.Data.Builder()
                            .putInt("threshold", threshold)
                            .putInt("budget", budget)
                            .putLong("expense", it)
                            .build()

                        val workRequest = OneTimeWorkRequestBuilder<BudgetNotificationWorker>()
                            .setInputData(inputData)
                            .setInitialDelay(5, TimeUnit.SECONDS)
                            .build()

                        androidx.work.WorkManager.getInstance(requireActivity())
                            .enqueue(workRequest)

                        prefs.edit().putBoolean("has_sent_budget_exceeded", true).apply()
                    }
                }
            }
        }
    }
}