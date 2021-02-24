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
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts;
import com.angogasapps.myfamily.firebase.interfaces.IAuthUser;
import com.angogasapps.myfamily.objects.User;
import com.angogasapps.myfamily.ui.screens.findorcreatefamily.FindOrCreateFamilyActivity;
import com.angogasapps.myfamily.ui.screens.main.MainActivity;
import com.angogasapps.myfamily.ui.screens.registeractivity.RegisterActivity;
import com.angogasapps.myfamily.ui.toaster.Toaster;
import com.angogasapps.myfamily.utils.FamilyManager;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.initFirebase;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.familyMembersMap;

public class SplashActivity extends AppCompatActivity {
    private String familyIdParam = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initFirebase();
    }

    @Override
    public void onStart() {
        super.onStart();
        start();
    }

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

    public void start(){
        if (AppApplication.isOnline()){
            if (AUTH.getCurrentUser() != null){
                //интернет есть, пользователь аторизован
                analysisIntent();
                AuthFunctions.downloadUser(this::onEndDownloadUser);
                //TODO: страховка с БД на случай слабого интернета
                // . . .
            }else{
                //интернет есть, пользователь не авторизован
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
            }
        }else {
            if (AUTH.getCurrentUser() != null) {

                // Вход с данными из БД
                signInWithRoom();


            }else{
                //интернета нет, пользователь не авторизован
                Toaster.error(this, R.string.connection_is_not).show();
            }
        }
    }

    public void signInWithRoom(){
        DatabaseManager.loadUsersAndMessages(() -> {
            for (User user: DatabaseManager.getUserList()){
                familyMembersMap.put(user.getId(), user);
            }
            if (familyMembersMap.containsKey(AUTH.getCurrentUser().getUid())){
                USER = familyMembersMap.get(AUTH.getCurrentUser().getUid());
            }

            if (USER.getFamily().equals("")) {
                Toaster.error(this, R.string.connection_is_not).show();
            }else{
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}