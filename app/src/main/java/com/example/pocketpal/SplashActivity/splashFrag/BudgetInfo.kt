package com.example.pocketpal.SplashActivity.splashFrag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.pocketpal.R
import com.example.pocketpal.database.Budget
import com.example.pocketpal.databinding.FragmentBudgetInfoBinding

class BudgetInfo : Fragment() {
    private lateinit var bind: FragmentBudgetInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_budget_info, container, false)

        bind.btnSave.setOnClickListener {
            val budget = bind.etBudget.text.toString().toLong()
            val income = bind.etIncome.text.toString().toLong()
            val data = Budget(budget, income)

            view?.findNavController()?.navigate(BudgetInfoDirections.actionBudgetInfoToRegisterFragment(data))
        }

        return bind.root
    }
}