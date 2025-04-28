package com.example.pocketpal.SplashActivity.splashFrag

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.pocketpal.MainActivity.MainActivity
import com.example.pocketpal.R
import com.example.pocketpal.SplashActivity.RegisterViewModel
import com.example.pocketpal.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    // it will show one time
    private lateinit var bind: FragmentRegisterBinding
    private lateinit var rvm: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        rvm = ViewModelProvider(requireActivity())[RegisterViewModel::class.java]
        val intent = Intent(requireActivity(), MainActivity::class.java)

        val tfFullName = bind.tfFullName
        val tfEmail = bind.tfEmail
        val tfPassword = bind.tfPassword
        val btnRegister = bind.btnRegister
        val cbPrivacy = bind.cbPrivacy

        val args: RegisterFragmentArgs by navArgs()
        val budget = args.budget.monthlyBudget
        val income = args.budget.income
        val imageName = args.budget.imageName

        btnRegister.setOnClickListener {
            if (cbPrivacy.isChecked && checkInputs()) {
                val fullName = tfFullName.text.toString()
                val email = tfEmail.text.toString()
                val password = tfPassword.text.toString()
                rvm.saveUserData(fullName, email, password, budget, income, imageName)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        return bind.root
    }

    private fun checkInputs(): Boolean {
        return bind.tfEmail.text!!.isNotEmpty() && bind.tfPassword.text!!.isNotEmpty() && bind.tfFullName.text!!.isNotEmpty()
    }
}