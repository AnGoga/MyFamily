package com.angogasapps.myfamily.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.ui.activites.MainActivity;
import com.angogasapps.myfamily.ui.activites.RegisterActivity;
import com.angogasapps.myfamily.ui.toaster.Toaster;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static com.angogasapps.myfamily.firebase.Functions.signInWithCredential;
import static com.angogasapps.myfamily.firebase.Vars.AUTH;

public class EnterPhoneFragment extends Fragment {
    private FloatingActionButton btnNext;
    private EditText editTextPhone;
    private String mPhoneNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;


    @Override
    public void onStart() {
        super.onStart();

        AUTH = FirebaseAuth.getInstance();

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                signInWithCredential(getActivity(), credential);
                }
            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toaster.error(getActivity(), "Auth Error: " + e.getMessage()).show();
            }

            @Override
            public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.registerDataContainer, new EnterCodeFragment(mPhoneNumber, id)).commit();
            }
        };

        btnNext.setOnClickListener((view) -> {
            if (editTextPhone.getText().toString().isEmpty()) {
                Toaster.error(getActivity(), R.string.enter_your_number_phone).show();
            } else {
                sentVerificationCode();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_enter_phone, container, false);

        btnNext = mView.findViewById(R.id.enterPhoneFragmentBtnNext);
        editTextPhone = mView.findViewById(R.id.enterPhoneFragmentInputPhone);

        return mView;
    }

    private void sentVerificationCode() {
        //enter code
        authUser();
    }

    private void authUser() {
        mPhoneNumber = editTextPhone.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mPhoneNumber,
                120, TimeUnit.SECONDS,
                ((RegisterActivity)getActivity()),
                mCallback
        );
    }
}