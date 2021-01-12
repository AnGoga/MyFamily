package com.angogasapps.myfamily.ui.screens.findorcreatefamily;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.ui.screens.findorcreatefamily.FindOrCreateFamilyFragment;
import com.angogasapps.myfamily.ui.screens.splash.SplashActivity;
import com.angogasapps.myfamily.utils.FamilyManager;

import java.util.Set;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class FindOrCreateFamilyActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_or_create_family);
    }

    @Override
    protected void onStart() {
        super.onStart();


        getSupportFragmentManager().beginTransaction()
                .add(R.id.findOrCreateFamilyDataContainer, new FindOrCreateFamilyFragment()).commit();

    }
}