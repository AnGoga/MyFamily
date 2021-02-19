package com.angogasapps.myfamily.ui.screens.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.database.DatabaseManager;
import com.angogasapps.myfamily.firebase.AuthFunctions;
import com.angogasapps.myfamily.firebase.interfaces.IAuthUser;
import com.angogasapps.myfamily.ui.screens.findorcreatefamily.FindOrCreateFamilyActivity;
import com.angogasapps.myfamily.ui.screens.main.MainActivity;
import com.angogasapps.myfamily.ui.screens.registeractivity.RegisterActivity;
import com.angogasapps.myfamily.ui.toaster.Toaster;
import com.angogasapps.myfamily.utils.FamilyManager;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.initFirebase;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class SplashActivity extends AppCompatActivity {
    private String familyIdParam = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initFirebase();
        DatabaseManager.init(this);
        DatabaseManager.loadUsersAndMessages();

    }

    @Override
    public void onStart() {
        super.onStart();

        IAuthUser iAuthUser = this::onEndDownloadUser;

        if (AUTH.getCurrentUser() != null) {
            // если пользователь уже авторизован, скачиваем данные про него, а потом пропускаем его в MainActivity
            if (AppApplication.isOnline()){
                analysisIntent();
            }else{
                iAuthUser = () -> {};
                onEndDownloadUser();
            }

            AuthFunctions.downloadUser(iAuthUser);
        } else {
            // если пользователь не авторизован, начинаем процес авторизации/регистрации
            if (AppApplication.isOnline()) {
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
            }else{
                Toaster.error(this, R.string.connection_is_not).show();
            }
        }
    }
    /*



        if (AUTH.getCurrentUser() != null) {
            // если пользователь уже авторизован, скачиваем данные про него, а потом пропускаем его в MainActivity

            analysisIntent();

            AuthFunctions.downloadUser(() -> {
                Log.d("tag", "\n" + USER.toString());


                if (USER.getFamily().equals("")){
                    Intent intent = new Intent(this, FindOrCreateFamilyActivity.class);
                    intent.putExtra(FamilyManager.PARAM_FAMILY_ID, familyIdParam);
                    startActivity(intent);
                }else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            });
        } else {
            // если пользователь не авторизован, начинаем процес авторизации/регистрации
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        }
     */

    private void analysisIntent() {

        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            familyIdParam = data.getQueryParameter(FamilyManager.PARAM_FAMILY_ID);

            System.out.println("\ndata = " + data.toString() + "\nfamily id = " + familyIdParam);

        }
    }
    private void onEndDownloadUser(){
        Log.d("tag", "\n" + USER.toString());


        if (USER.getFamily().equals("")){
            Intent intent = new Intent(this, FindOrCreateFamilyActivity.class);
            intent.putExtra(FamilyManager.PARAM_FAMILY_ID, familyIdParam);
            startActivity(intent);
        }else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
    }
}