package com.example.pocketpal.MainActivity.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pocketpal.MainActivity.MainViewModel
import com.example.pocketpal.MainActivity.MainViewModelFactory
import com.example.pocketpal.R
import com.example.pocketpal.database.ExpenseDatabase
import com.example.pocketpal.database.ExpenseRepository
import com.example.pocketpal.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Home : Fragment() {
    private lateinit var bind: FragmentHomeBinding
    private lateinit var mainViewModel: MainViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val expenseDatabase = ExpenseDatabase.getDatabase(requireActivity())
        val expenseRepository = ExpenseRepository(expenseDatabase)
        mainViewModel =
            ViewModelProvider(
                requireActivity(),
                MainViewModelFactory(expenseRepository)
            )[MainViewModel::class.java]

        mainViewModel.getUserDetails().observe(viewLifecycleOwner) {
            if (it != null && it.fullName.isNotEmpty()) {
                val fullName = it.fullName
                val firstName = fullName.trim().substringBefore(" ").replaceFirstChar { c ->
                    return@replaceFirstChar c.uppercase()
                }
                val profileImageDrawable = requireContext()
                    .resources
                    .getIdentifier(
                        it.imageName, "drawable", requireContext().packageName
                    )
                bind.apply {
                    spentAmount.text = it.monthlyBudget.toString()
                    initials.text = "Hi,$firstName"
                    userProfile.setImageResource(profileImageDrawable)
                }

            }
        }

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<CardView>(R.id.nu_cvBottomNavigation)?.let {
            it.visibility = View.VISIBLE
        }
        requireActivity().findViewById<FloatingActionButton>(R.id.addItem)?.let{
            it.visibility = View.VISIBLE
        }

    }

}