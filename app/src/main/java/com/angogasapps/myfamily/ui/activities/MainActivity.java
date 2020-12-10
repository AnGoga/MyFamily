package com.angogasapps.myfamily.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.angogasapps.myfamily.R;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.USER;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (USER.family.equals("")) {
            startActivity(new Intent(this, FindOrCreateFamilyActivity.class));
            finish();
        }

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
    }
}