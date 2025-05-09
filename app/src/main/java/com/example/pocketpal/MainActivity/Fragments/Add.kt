package com.example.pocketpal.MainActivity.Fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        requireActivity().findViewById<CardView>(R.id.nu_cvBottomNavigation).visibility = View.GONE
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind.cvAddManually.setOnClickListener {
            findNavController().navigate(R.id.action_add_to_expenseDetails)
        }

        bind.cvScanBarcode.setOnClickListener {
            scanOCRReceipt()
        }
    }

    private fun scanOCRReceipt() {
        if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            findNavController().navigate(R.id.action_add_to_OCR)
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), 102)
        }
    }
}