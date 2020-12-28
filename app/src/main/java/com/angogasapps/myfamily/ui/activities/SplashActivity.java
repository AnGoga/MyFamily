package com.angogasapps.myfamily.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.angogasapps.myfamily.firebase.AuthFunctions;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndSetUserField;
import com.angogasapps.myfamily.ui.toaster.Toaster;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.USER;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.initFirebase;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //инициализируем Firebase
        initFirebase();
        //setTheme(AppCompatDelegate.MODE_NIGHT_YES);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (AUTH.getCurrentUser() != null) {
            // если пользователь уже авторизован, скачиваем данные про него, а потом пропускаем его в MainActivity

            AuthFunctions.downloadUser(() -> {
                Log.d("tag", "\n" + USER.toString());
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            });
        } else {
            // если пользователь не авторизован, начинаем процес авторизации/регистрации
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        }
    }
}