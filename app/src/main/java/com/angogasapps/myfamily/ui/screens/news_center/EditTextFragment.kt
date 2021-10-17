package com.angogasapps.myfamily.ui.screens.news_center

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.angogasapps.myfamily.databinding.FragmentEditTextBinding

class EditTextFragment : Fragment() {
    private lateinit var binding: FragmentEditTextBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditTextBinding.inflate(inflater, container, false)
        return binding.root
    }

    val text: String
        get() = binding.editText.text.toString()

    fun resetEditText() {
        binding.editText.setText("")
    }
}