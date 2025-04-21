package com.example.pocketpal.MainActivty.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pocketpal.MainActivty.MainViewModel
import com.example.pocketpal.MainActivty.MainViewModelFactory
import com.example.pocketpal.R
import com.example.pocketpal.database.ExpenseDatabase
import com.example.pocketpal.database.ExpenseRepository
import com.example.pocketpal.databinding.FragmentBudgetInfoBinding
import com.example.pocketpal.databinding.FragmentHomeBinding

class Home : Fragment() {
    private lateinit var bind: FragmentHomeBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val expenseDatabase = ExpenseDatabase.getDatabase(requireActivity())
        val expenseRepository = ExpenseRepository(expenseDatabase)
        mainViewModel =
            ViewModelProvider(requireActivity(), MainViewModelFactory(expenseRepository))[MainViewModel::class.java]

        mainViewModel.getUserDetails().observe(viewLifecycleOwner){
            if (it != null && it.fullName.isNotEmpty()){
                val fullName = it.fullName
                val firstName = fullName.trim().substringBefore(" ")
                bind.initials.text = "Hi, $firstName"
            }
        }

        return bind.root
    }
}