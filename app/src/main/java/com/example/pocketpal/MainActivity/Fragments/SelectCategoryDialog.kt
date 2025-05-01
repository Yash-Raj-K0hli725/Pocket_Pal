package com.example.pocketpal.MainActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.pocketpal.R
import com.example.pocketpal.databinding.FragmentSelectCategoryDialogBinding


class SelectCategoryDialog : DialogFragment() {
    private lateinit var bind: FragmentSelectCategoryDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_select_category_dialog, container, false)

        return bind.root
    }
}