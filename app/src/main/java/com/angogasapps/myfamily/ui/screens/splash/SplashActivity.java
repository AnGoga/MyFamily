package com.angogasapps.myfamily.ui.screens.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.angogasapps.myfamily.firebase.AuthFunctions;
import com.angogasapps.myfamily.ui.screens.main.MainActivity;
import com.angogasapps.myfamily.ui.screens.registeractivity.RegisterActivity;
import com.angogasapps.myfamily.utils.FamilyManager;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.initFirebase;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

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

            analysisLink();

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

    private void analysisLink() {

        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            String familyIdParam = data.getQueryParameter(FamilyManager.PARAM_FAMILY_ID);


            System.out.println("\ndata = " + data.toString() + "\nfamily id = " + familyIdParam);
        }
    }
}