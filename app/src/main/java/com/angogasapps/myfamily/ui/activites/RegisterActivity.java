package com.angogasapps.myfamily.ui.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.ui.fragments.EnterPersonalDataFragment;
import com.angogasapps.myfamily.ui.fragments.EnterPhoneFragment;
import com.angogasapps.myfamily.ui.toaster.Toaster;


public class RegisterActivity extends AppCompatActivity {
    public static INewUser iNewUser;
    @Override
    protected void onStart() {
        super.onStart();
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
        Toaster.success(activity, R.string.welcome).show();
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }
    public interface INewUser{
        void getUserPersonalData();
    }
}