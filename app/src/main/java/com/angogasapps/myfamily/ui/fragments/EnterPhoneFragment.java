package com.angogasapps.myfamily.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.ui.toaster.Toaster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.zip.Inflater;


public class EnterPhoneFragment extends Fragment {
    FloatingActionButton btnNext;
    EditText editTextPhone;

    @Override
    public void onStart() {
        super.onStart();

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

        //replace fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.registerDataContainer, new EnterCodeFragment()).commit();
    }
}