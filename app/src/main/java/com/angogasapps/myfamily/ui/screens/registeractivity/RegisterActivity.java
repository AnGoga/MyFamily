package com.angogasapps.myfamily.ui.screens.registeractivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.ui.screens.splash.SplashActivity;
import com.angogasapps.myfamily.ui.toaster.Toaster;


public class RegisterActivity extends AppCompatActivity {
    public static INewUser iNewUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        iNewUser = () -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.registerDataContainer, new EnterPersonalDataFragment()).commit();
        };
        // запускаем фрагмент, отвечающий за ввод пользователем его номера телефона
        getSupportFragmentManager().beginTransaction().add(R.id.registerDataContainer, new EnterPhoneFragment()).commit();

    }


    public static void welcomeFunc(Activity activity) {
        Toaster.success(activity, activity.getString(R.string.welcome)).show();
        activity.startActivity(new Intent(activity, SplashActivity.class));
        activity.finish();
    }
    public interface INewUser{
        void getUserPersonalData();
    }
}