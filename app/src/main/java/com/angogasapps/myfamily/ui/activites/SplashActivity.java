package com.angogasapps.myfamily.ui.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (false) {
            startActivity(new Intent(this, MainActivity.class));
        }else{
            startActivity(new Intent(this, RegisterActivity.class));
        }
        finish();
    }
}