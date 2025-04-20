package com.example.pocketpal.SplashActivity.splashFrag.introVPFrags

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pocketpal.MainActivty.MainActivity
import com.example.pocketpal.R
import com.example.pocketpal.SplashActivity.RegisterViewModel
import com.example.pocketpal.SplashActivity.RegisterViewModelFactory
import com.example.pocketpal.SplashActivity.data.dataStore.DataStoreManager
import com.example.pocketpal.SplashActivity.data.repository.UserRepository
import com.example.pocketpal.database.ExpenseDatabase
import com.example.pocketpal.databinding.FragmentSplashBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashFragment : Fragment() {
    private lateinit var bind: FragmentSplashBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)

        val expenseDatabase = ExpenseDatabase.getDatabase(requireActivity())
        val databaseDao = expenseDatabase.databaseDao()
        val userRepository = UserRepository(databaseDao)
        registerViewModel =
            ViewModelProvider(
                requireActivity(),
                RegisterViewModelFactory(userRepository)
            )[RegisterViewModel::class
                .java]

        registerViewModel.getUserDetails().observe(viewLifecycleOwner) { user ->
            lifecycleScope.launch {
                delay(1000)

                if (user != null && user.fullName.isNotEmpty()) {
                    launchMainActivity()
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_introPage)
                }
            }
        }


        return bind.root
    }

    private fun launchMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().apply {
            overridePendingTransition(R.anim.nav_fade_in, R.anim.nav_fade_out)
            finish()
        }
    }
}