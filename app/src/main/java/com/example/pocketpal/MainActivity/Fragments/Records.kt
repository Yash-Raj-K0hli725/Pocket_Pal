package com.example.pocketpal.MainActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketpal.MainActivity.MainViewModel
import com.example.pocketpal.MainActivity.MainViewModelFactory
import com.example.pocketpal.MainActivity.RecordsAdapter
import com.example.pocketpal.R
import com.example.pocketpal.database.ExpenseDatabase
import com.example.pocketpal.database.ExpenseRepository
import com.example.pocketpal.databinding.FragmentRecordsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class Records : Fragment() {
    private lateinit var bind: FragmentRecordsBinding
    private lateinit var mainViewModel: MainViewModel
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
    }

    private fun recordsTopDetails() {
        mainViewModel.totalExpense().observe(viewLifecycleOwner) {
            if (it != null) {
                bind.expenseText.text = "-₹${it.toString()}"
            } else {
                bind.expenseText.text = "-₹0"
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
        val adapter = RecordsAdapter(mainViewModel, requireContext(), navController){ expense ->
            findNavController().navigate(
                RecordsDirections.actionMiRecordsToEDDialog(expense)
            )
        }
        bind.apply {
            rvExpense.adapter = adapter
            rvExpense.layoutManager = LinearLayoutManager(requireActivity())
        }
        mainViewModel.expenseDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it)
            }
        }
    }
}