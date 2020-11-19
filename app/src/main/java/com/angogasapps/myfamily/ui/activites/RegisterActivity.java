package com.angogasapps.myfamily.ui.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.ui.fragments.EnterCodeFragment;
import com.angogasapps.myfamily.ui.fragments.EnterPhoneFragment;

public class RegisterActivity extends AppCompatActivity {


    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_register);

        getSupportFragmentManager().beginTransaction().add(R.id.registerDataContainer, new EnterPhoneFragment()).commit();


    }
}