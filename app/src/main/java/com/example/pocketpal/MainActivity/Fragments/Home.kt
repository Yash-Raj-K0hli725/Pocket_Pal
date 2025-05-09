package com.example.pocketpal.MainActivity.Fragments

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketpal.MainActivity.MainViewModel
import com.example.pocketpal.MainActivity.MainViewModelFactory
import com.example.pocketpal.MainActivity.RecentTransactionsAdapter
import com.example.pocketpal.R
import com.example.pocketpal.database.ExpenseDatabase
import com.example.pocketpal.database.ExpenseRepository
import com.example.pocketpal.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

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
                val profileImageDrawable = requireActivity()
                    .resources
                    .getIdentifier(
                        it.imageName, "drawable", requireActivity().packageName
                    )
                bind.apply {
                    initials.text = "Hi,$firstName"
                    userProfile.setImageResource(profileImageDrawable)
                }

            }
        }
        recentTransactionsAdapter()

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

        mainViewModel.totalExpense().observe(viewLifecycleOwner) {
            Log.d("check", "onViewCreated: $it")
            if (it != null) {
                Log.d("check", "onViewCreated: $it")
                bind.spentAmount.text = "₹$it"
            }
        }
    }

    private fun recentTransactionsAdapter(){
        val adapter = RecentTransactionsAdapter(requireActivity())

        bind.apply {
            recentTransactions.adapter = adapter
            recentTransactions.layoutManager = LinearLayoutManager(requireActivity())
        }

        mainViewModel.recentTransactions.observe(viewLifecycleOwner){
            Log.d("charu", it.toString())
            if (it != null){
                adapter.submitList(it)
            }
        }
    }

}