package com.example.pocketpal.MainActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pocketpal.R
import com.example.pocketpal.databinding.FragmentAddBinding

class Add : Fragment() {
    private lateinit var bind: FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false)
        bind.cvAddManually.setOnClickListener {
            findNavController().navigate(R.id.action_add_to_expenseDetails)
        }
        requireActivity().findViewById<CardView>(R.id.nu_cvBottomNavigation).visibility = View.GONE
        return bind.root
    }
}