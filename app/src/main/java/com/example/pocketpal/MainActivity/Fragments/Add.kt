package com.example.pocketpal.MainActivity.Fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.work.OneTimeWorkRequestBuilder
import com.example.pocketpal.MainActivity.BudgetNotificationWorker
import com.example.pocketpal.MainActivity.MainViewModel
import com.example.pocketpal.MainActivity.MainViewModelFactory
import com.example.pocketpal.R
import com.example.pocketpal.database.ExpenseDatabase
import com.example.pocketpal.database.ExpenseRepository
import com.example.pocketpal.databinding.FragmentAddBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class Add : Fragment() {
    private lateinit var bind: FragmentAddBinding
    private lateinit var mainViewModel: MainViewModel
    private var budget: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false)
        val database = ExpenseDatabase.getDatabase(requireActivity())
        val expenseRepository = ExpenseRepository(database)
        mainViewModel =
            ViewModelProvider(requireActivity(), MainViewModelFactory(expenseRepository))[MainViewModel::class.java]

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            sendNotification()
        }

        requireActivity().findViewById<CardView>(R.id.nu_cvBottomNavigation).visibility = View.GONE
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind.cvAddManually.setOnClickListener {
            findNavController().navigate(R.id.action_add_to_expenseDetails)
        }

        bind.cvScanBarcode.setOnClickListener {
            scanOCRReceipt()
        }
    }

    private fun scanOCRReceipt() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            findNavController().navigate(R.id.action_add_to_OCR)
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 102)
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