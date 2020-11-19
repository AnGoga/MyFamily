package com.angogasapps.myfamily.firebase;

import android.app.Activity;
import android.content.Intent;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.ui.activites.MainActivity;
import com.angogasapps.myfamily.ui.toaster.Toaster;
import com.google.firebase.auth.PhoneAuthCredential;

import static com.angogasapps.myfamily.firebase.Vars.AUTH;

public class Functions {

    public static void signInWithCredential(Activity activity, PhoneAuthCredential credential){
        AUTH.signInWithCredential(credential).addOnCompleteListener(authResultTask -> {
            if (authResultTask.isSuccessful()) {
                //Если верификация пользователя прошла успешно
                Toaster.success(activity, R.string.welcome).show();

                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.finish();
            } else {
                //Если в верификации произошла ошибка
                Toaster.error(activity, authResultTask.getException().toString()).show();
            }
        });
    }
}
