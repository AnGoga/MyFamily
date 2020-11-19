package com.angogasapps.myfamily.ui.activites;

import android.app.Activity;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.ui.fragments.EnterPhoneFragment;
import com.angogasapps.myfamily.ui.toaster.Toaster;


public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_register);
        // запускаем фрагмент, отвечающий за ввод пользователем его номера телефона
        getSupportFragmentManager().beginTransaction().add(R.id.registerDataContainer, new EnterPhoneFragment()).commit();

    }
    /**
     * Данная функция вызывается 2 раза в файле AuthFunctions,
     * нужна для перехода из RegisterActivity в Main Activity
     */
    public static void welcomeFunc(Activity activity){
        Toaster.success(activity, R.string.welcome).show();

        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }
}