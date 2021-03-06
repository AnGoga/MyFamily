package com.angogasapps.myfamily.ui.screens.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.async.notification.TokensManager;
import com.angogasapps.myfamily.database.DatabaseManager;
import com.angogasapps.myfamily.firebase.AuthFunctions;
import com.angogasapps.myfamily.firebase.interfaces.IAuthUser;
import com.angogasapps.myfamily.models.Family;
import com.angogasapps.myfamily.ui.screens.findorcreatefamily.FindOrCreateFamilyActivity;
import com.angogasapps.myfamily.ui.screens.main.MainActivity;
import com.angogasapps.myfamily.ui.screens.registeractivity.RegisterActivity;
import com.angogasapps.myfamily.utils.FamilyManager;

import es.dmoral.toasty.Toasty;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.initFirebase;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

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

        TokensManager.getInstance().updateToken(USER);


        if (USER.getFamily().equals("")){
            Intent intent = new Intent(this, FindOrCreateFamilyActivity.class);
            intent.putExtra(FamilyManager.PARAM_FAMILY_ID, familyIdParam);
            startActivity(intent);
        }else {
//            startActivity(new Intent(SplashActivity.this, DeprecatedMainActivity.class));
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
    }

    private void onError(){
        start();
    }

    public void start(){
        if (AppApplication.isOnline()){
            if (AUTH.getCurrentUser() != null){
                //интернет есть, пользователь аторизован
                analysisIntent();
                AuthFunctions.downloadUser(new IAuthUser() {
                    @Override
                    public void onEndDownloadUser() {
                        SplashActivity.this.onEndDownloadUser();
                    }

                    @Override
                    public void onError() {
                        SplashActivity.this.onError();
                    }
                });
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
                Toasty.error(this, R.string.connection_is_not).show();
            }
        }
    }

    public void signInWithRoom(){
        DatabaseManager.comeInByDatabase(() -> {
            Family.getInstance().setUsersList(DatabaseManager.getUserList());
            if (Family.getInstance().containsUserWithId(AUTH.getCurrentUser().getUid())){
                USER = Family.getInstance().getUserById(AUTH.getCurrentUser().getUid());
            }

            if (USER.getFamily().equals("")) {
                Toasty.error(this, R.string.connection_is_not).show();
            }else{
//                startActivity(new Intent(SplashActivity.this, DeprecatedMainActivity.class));
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}