package com.example.pocketpal.MainActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.pocketpal.R
import com.example.pocketpal.databinding.FragmentExpenseDetailsBinding

class ExpenseDetails : Fragment() {
    private lateinit var bind: FragmentExpenseDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_expense_details, container, false)

        bind.apply {
            rbIncome.visibility = View.GONE
            rbExpense.isChecked = true
            rbTransfer.visibility = View.GONE
            tvTransfer.visibility = View.VISIBLE

            tvIncome.setOnClickListener {
                rbIncome.isChecked = true
                rbIncome.visibility = View.VISIBLE
                rbExpense.visibility = View.GONE
                rbTransfer.visibility = View.GONE
            }

            tvExpense.setOnClickListener {
                rbExpense.isChecked = true
                rbIncome.visibility = View.GONE
                rbExpense.visibility = View.VISIBLE
                rbTransfer.visibility = View.GONE
            }

            tvTransfer.setOnClickListener {
                rbTransfer.isChecked = true
                rbIncome.visibility = View.GONE
                rbExpense.visibility = View.GONE
                rbTransfer.visibility = View.VISIBLE
            }
        }

        return bind.root
    }
}