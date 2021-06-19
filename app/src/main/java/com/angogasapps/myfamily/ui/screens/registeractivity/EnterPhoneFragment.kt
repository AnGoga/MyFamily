package com.angogasapps.myfamily.ui.screens.registeractivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.FragmentEnterPhoneBinding
import com.angogasapps.myfamily.firebase.AuthFunctions.authorizationUser
import com.angogasapps.myfamily.firebase.AuthFunctions.trySignInWithCredential
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts
import com.angogasapps.myfamily.ui.screens.splash.SplashActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import es.dmoral.toasty.Toasty
import java.util.concurrent.TimeUnit

class EnterPhoneFragment(val onNewUserSignIn: () -> Unit, val onOldUserSignIn: () -> Unit) : Fragment() {
    private lateinit var binding: FragmentEnterPhoneBinding
    private var mPhoneNumber: String? = null
    private var mCallback: OnVerificationStateChangedCallbacks? = null

    override fun onStart() {
        super.onStart()
        FirebaseVarsAndConsts.AUTH = FirebaseAuth.getInstance()

        mCallback = object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                trySignInWithCredential(
                        activity,
                        credential,
                        onNewUserSignIn = onNewUserSignIn,
                        onOldUserSignIn = onOldUserSignIn
                )
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toasty.error(activity!!, "Auth Error: " + e.message).show()
            }

            override fun onCodeSent(id: String, forceResendingToken: ForceResendingToken) {
                this@EnterPhoneFragment.activity
                        ?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(
                                R.id.registerDataContainer,
                                EnterCodeFragment(
                                        mPhoneNumber?:"",
                                        id,
                                        onNewUserSignIn = onNewUserSignIn,
                                        onOldUserSignIn = onOldUserSignIn
                                )
                        )?.addToBackStack("")?.commit()
            }
        }
        binding.btnNext.setOnClickListener { view: View? ->
            if (binding.phoneEditText.text.toString().isEmpty()) {
                Toasty.error(requireActivity(), R.string.enter_your_number_phone).show()
            } else {
                mPhoneNumber = binding.phoneEditText.text.toString()
                authorizationUser(binding.phoneEditText.text.toString(), 60, TimeUnit.SECONDS, activity, mCallback)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentEnterPhoneBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}