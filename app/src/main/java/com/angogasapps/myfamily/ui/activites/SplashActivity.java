package com.angogasapps.myfamily.ui.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import static com.angogasapps.myfamily.firebase.Vars.AUTH;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //получаем пользователя
        AUTH = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart(){
        super.onStart();

        if (AUTH.getCurrentUser() != null) {
            // если пользователь уже авторизован
            startActivity(new Intent(this, MainActivity.class));
        }else{
            // если пользователь не автаризован
            startActivity(new Intent(this, RegisterActivity.class));
        }
        finish();

    }
}