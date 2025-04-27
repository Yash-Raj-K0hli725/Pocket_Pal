package com.example.pocketpal.SplashActivity.splashFrag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.pocketpal.R
import com.example.pocketpal.database.Budget
import com.example.pocketpal.databinding.FragmentBudgetInfoBinding

class BudgetInfo : Fragment() {
    private lateinit var bind: FragmentBudgetInfoBinding
    private lateinit var Observer:LifecycleEventObserver
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_budget_info, container, false)

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.apply {
            etBudget.setHint(R.string.budget_field)
            etIncome.setHint(R.string.budget_field)
            btnSave.setOnClickListener {
                if (checkInputs()) {
                    val budget = etBudget.text.toString().toLong()
                    val income = etIncome.text.toString().toLong()
                    val data = Budget(budget, income)
                    findNavController().navigate(
                        BudgetInfoDirections.actionBudgetInfoToRegisterFragment(
                            data
                        )
                    )
                }
            }
            val navController = findNavController().getBackStackEntry(R.id.budgetInfo)

            Observer = LifecycleEventObserver { _, event ->
                Log.d("yash",navController.savedStateHandle.contains("pfpName").toString())
                if (event == Lifecycle.Event.ON_RESUME && navController.savedStateHandle.contains("pfpName")) {
                    val profile = navController.savedStateHandle.get<String>("pfpName")
                    Log.d("yash","yo $profile")
                }
            }
            viewLifecycleOwner.lifecycle.addObserver(Observer)

            selectProfile.setOnClickListener {
                findNavController().navigate(R.id.action_budgetInfo_to_selectProfileDialog)
            }

        }

    }

    private fun checkInputs(): Boolean =
        bind.etBudget.text!!.isNotEmpty() && bind.etIncome.text!!.isNotEmpty()

    override fun onDestroy() {
        super.onDestroy()
        viewLifecycleOwner.lifecycle.removeObserver(Observer)
    }

}