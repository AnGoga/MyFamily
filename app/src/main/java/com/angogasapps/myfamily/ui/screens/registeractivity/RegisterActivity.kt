package com.angogasapps.myfamily.ui.screens.registeractivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.async.LoadFamilyThread;
import com.angogasapps.myfamily.ui.screens.splash.SplashActivity;


import es.dmoral.toasty.Toasty;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;


public class RegisterActivity extends AppCompatActivity {
    public static INewUser iNewUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        iNewUser = () -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.registerDataContainer, new EnterPersonalDataFragment())
                    .addToBackStack("")
                    .commit();
        };

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.registerDataContainer, new EnterPhoneFragment())
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_signout){
            AUTH.signOut();
            startActivity(new Intent(this, SplashActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public static void welcomeFunc(Activity activity) {
        //TODO
//        new LoadFamilyThread(activity).execute(USER);
        Toasty.success(activity, activity.getString(R.string.welcome)).show();
        activity.startActivity(new Intent(activity, SplashActivity.class));
        activity.finish();
    }
    public interface INewUser{
        void getUserPersonalData();
    }
}