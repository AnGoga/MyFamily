package com.angogasapps.myfamily.ui.screens.registeractivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.angogasapps.myfamily.databinding.FragmentEnterCodeBinding
import com.angogasapps.myfamily.firebase.AuthFunctions
import com.google.firebase.auth.PhoneAuthProvider

class EnterCodeFragment(private val mPhoneNumber: String, private val id: String,
                        val onNewUserSignIn: () -> Unit, val onOldUserSignIn: () -> Unit) : Fragment() {

    private lateinit var binding: FragmentEnterCodeBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentEnterCodeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.codeEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val code = binding.codeEditText.text.toString()
                if (code.length == 6) {
                    enterCode()
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun enterCode() {
        val code = binding.codeEditText.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AuthFunctions.trySignInWithCredential(activity, credential, onNewUserSignIn = onNewUserSignIn, onOldUserSignIn = onOldUserSignIn)
    }
}