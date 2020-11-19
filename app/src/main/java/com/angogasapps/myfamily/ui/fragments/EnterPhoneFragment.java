package com.angogasapps.myfamily.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.ui.toaster.Toaster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static com.angogasapps.myfamily.firebase.AuthFunctions.authorizationUser;
import static com.angogasapps.myfamily.firebase.AuthFunctions.trySignInWithCredential;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.AUTH;

public class EnterPhoneFragment extends Fragment {
    private EditText editTextPhone;
    FloatingActionButton btnNext;
    private String mPhoneNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    @Override
    public void onStart() {
        super.onStart();

        AUTH = FirebaseAuth.getInstance();
        //callback который сработает после верификации пользователя
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            @Override
            // если верификация успешная
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                trySignInWithCredential(getActivity(), credential);
            }
            @Override
            //в верификации произошла ошибка
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toaster.error(getActivity(), "Auth Error: " + e.getMessage()).show();
            }

            @Override
            //выполниться когда смс с кодом только отправят
            public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.registerDataContainer, new EnterCodeFragment(mPhoneNumber, id)).commit();
            }
        };

        // кнопка на EnterPhoneFragment для отправки сообщения с кодом
        btnNext.setOnClickListener((view) -> {
            // !!!!!
            // if  editTextPhone.getText().toString().isCorrectNumberPhone <-- напиши эту функцию!
            //!!!!!
            if (editTextPhone.getText().toString().isEmpty()) {
                Toaster.error(getActivity(), R.string.enter_your_number_phone).show();
            } else {
                authorizationUser(editTextPhone.getText().toString(), 120, TimeUnit.SECONDS, getActivity(), mCallback);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_enter_phone, container, false);
        editTextPhone = mView.findViewById(R.id.enterPhoneFragmentInputPhone);
        btnNext = mView.findViewById(R.id.enterPhoneFragmentBtnNext);
        return mView;
    }
}