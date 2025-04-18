package com.example.pocketpal.SplashActivity.splashFrag

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pocketpal.MainActivity
import com.example.pocketpal.R
import com.example.pocketpal.SplashActivity.RegisterViewModel
import com.example.pocketpal.SplashActivity.RegisterViewModelFactory
import com.example.pocketpal.SplashActivity.data.dataStore.DataStoreManager
import com.example.pocketpal.SplashActivity.data.repository.UserRepository
import com.example.pocketpal.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var bind: FragmentRegisterBinding
    private lateinit var rvm: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        val dataStore = DataStoreManager(requireContext())
        val repo = UserRepository(dataStore)
        rvm = ViewModelProvider(this, RegisterViewModelFactory(repo))[RegisterViewModel::class.java]
        val intent = Intent(requireActivity(), MainActivity::class.java)

        val tfFullName = bind.fullName
        val tfEmail = bind.email
        val tfPassword = bind.password
        val btnRegister = bind.btnRegister
        val cbPrivacy = bind.cbPrivacy

        btnRegister.setOnClickListener {
            if (cbPrivacy.isChecked) {
                val fullName = tfFullName.text.toString()
                val email = tfEmail.text.toString()
                val password = tfPassword.text.toString()
                rvm.saveUserData(fullName, email, password)
                startActivity(intent)
                requireActivity().finish()
            }
        }
        return bind.root
    }
}