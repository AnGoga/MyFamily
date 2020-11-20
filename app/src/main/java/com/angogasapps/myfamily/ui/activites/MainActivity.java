package com.angogasapps.myfamily.ui.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.percentlayout.widget.PercentRelativeLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.AuthFunctions;
import com.angogasapps.myfamily.ui.customview.CardView;

import java.util.Objects;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.USER;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthFunctions.downloadUser();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
/*
        if(USER.family.equals("")){
            startActivity(new Intent(this, null));
            finish();
        }*/
    }
}