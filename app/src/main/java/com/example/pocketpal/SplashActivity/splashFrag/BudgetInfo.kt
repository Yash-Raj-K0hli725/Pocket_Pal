package com.example.pocketpal.SplashActivity.splashFrag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import com.example.pocketpal.R
import com.example.pocketpal.database.Budget
import com.example.pocketpal.databinding.FragmentBudgetInfoBinding

class BudgetInfo : Fragment() {
    private lateinit var bind: FragmentBudgetInfoBinding
    private var imageName: String = ""
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
                    val data = Budget(budget, income, imageName)
                    findNavController().navigate(
                        BudgetInfoDirections.actionBudgetInfoToRegisterFragment(
                            data
                        )
                    )
                }
            }
            selectProfile.setOnClickListener {
                findNavController().navigate(R.id.action_budgetInfo_to_selectProfileDialog)
            }
            getImageResource()
        }

    }

    private fun getImageResource() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.budgetInfo)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains("pfpName")
            ) {
                imageName = navBackStackEntry.savedStateHandle.get("pfpName")!!
                val imageDrawable = requireContext().resources.getIdentifier(
                    imageName,
                    "drawable",
                    requireContext().packageName
                )
                bind.selectProfile.setImageResource(imageDrawable)
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }

        })

    }

    private fun checkInputs(): Boolean =
        bind.etBudget.text!!.isNotEmpty() && bind.etIncome.text!!.isNotEmpty() && imageName.isNotEmpty()
}