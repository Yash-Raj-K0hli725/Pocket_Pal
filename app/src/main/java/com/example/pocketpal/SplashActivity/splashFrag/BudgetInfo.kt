package com.example.pocketpal.SplashActivity.splashFrag

import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isNotEmpty
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.pocketpal.R
import com.example.pocketpal.SplashActivity.ProfileImageDialog
import com.example.pocketpal.database.Budget
import com.example.pocketpal.databinding.FragmentBudgetInfoBinding

class BudgetInfo : Fragment(), ProfileImageDialog.OnOptionSelectedListener{
    private lateinit var bind: FragmentBudgetInfoBinding
    private var profileInt : Int ?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_budget_info, container, false)

        bind.btnSave.setOnClickListener {
            val etBudget = bind.etBudget.text.toString()
            val etIncome = bind.etIncome.text.toString()
            if (etBudget.isNotEmpty() && etIncome.isNotEmpty()){
                val budget = etBudget.toLong()
                val income = etIncome.toLong()
                val data = Budget(budget, income)
                view?.findNavController()?.navigate(BudgetInfoDirections.actionBudgetInfoToRegisterFragment(data))
            }
            Log.d("check", "image id = $profileInt")
        }

        bind.addImage.setOnClickListener{
            val dialog = ProfileImageDialog(requireActivity(), this)
            dialog.show()
        }

        return bind.root
    }

    override fun onOptionSelected(selectedOption: Int) {
        Log.d("DialogResult", "User selected: $selectedOption")
        profileInt = selectedOption
    }
}