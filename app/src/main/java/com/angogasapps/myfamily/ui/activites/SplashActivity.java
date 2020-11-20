package com.angogasapps.myfamily.ui.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import static com.angogasapps.myfamily.firebase.AuthFunctions.downloadUser;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.initFirebase;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //инициализируем Firebase
        initFirebase();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (AUTH.getCurrentUser() != null) {
            // если пользователь уже авторизован, пропускаем его в MainActivity
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // если пользователь не автаризован, начинаем процес авторизации/регистрации
            startActivity(new Intent(this, RegisterActivity.class));
        }
        finish();
    }
}