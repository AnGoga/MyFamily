package com.angogasapps.myfamily.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.ui.fragments.registeractivity.EnterPersonalDataFragment;
import com.angogasapps.myfamily.ui.fragments.registeractivity.EnterPhoneFragment;
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
    /**
     * Данная функция вызывается 2 раза в файле AuthFunctions,
     * нужна для перехода из EnterCodeFragment в EnterPersonalDataFragment если пользователь новый
     * и для перехода из RegisterActivity в MainActivity если пользователь старый
     */
    public static void welcomeFunc(Activity activity) {
        Toaster.success(activity, activity.getString(R.string.welcome)).show();
        activity.startActivity(new Intent(activity, SplashActivity.class));
        activity.finish();
    }
    public interface INewUser{
        void getUserPersonalData();
    }
}