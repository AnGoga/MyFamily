package com.angogasapps.myfamily.ui.screens.registeractivity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.FragmentEnterPhoneBinding;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

import static com.angogasapps.myfamily.firebase.AuthFunctions.authorizationUser;
import static com.angogasapps.myfamily.firebase.AuthFunctions.trySignInWithCredential;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;

public class EnterPhoneFragment extends Fragment {
    private FragmentEnterPhoneBinding binding;
    private String mPhoneNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    @Override
    public void onStart() {
        super.onStart();

        Activity context = getActivity();

        AUTH = FirebaseAuth.getInstance();
        //callback, который сработает после верификации пользователя
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            @Override
            // если верификация успешная
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                trySignInWithCredential(context, credential);
            }
            @Override
            //в верификации произошла ошибка
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toasty.error(EnterPhoneFragment.this.getActivity(), "Auth Error: " + e.getMessage()).show();
            }

            @Override
            //выполниться когда смс с кодом только что отправили
            public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                EnterPhoneFragment.this.getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.registerDataContainer, new EnterCodeFragment(mPhoneNumber, id)).commit();
            }
        };

        // кнопка на EnterPhoneFragment для отправки сообщения с кодом
        binding.btnNext.setOnClickListener((view) -> {
            // !!!!!
            // TODO:
            // if  editTextPhone.getText().toString().isCorrectNumberPhone <-- напиши эту функцию!
            //!!!!!
            if (binding.phoneEditText.getText().toString().isEmpty()) {
                Toasty.error(getActivity(), R.string.enter_your_number_phone).show();
            } else {
                authorizationUser(binding.phoneEditText.getText().toString(), 60, TimeUnit.SECONDS, getActivity(), mCallback);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEnterPhoneBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }
}