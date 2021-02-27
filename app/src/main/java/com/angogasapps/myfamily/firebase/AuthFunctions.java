package com.angogasapps.myfamily.firebase;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.angogasapps.myfamily.firebase.interfaces.IAuthUser;
import com.angogasapps.myfamily.objects.User;
import com.angogasapps.myfamily.ui.screens.registeractivity.RegisterActivity;
import com.angogasapps.myfamily.ui.toaster.Toaster;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_PHONE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DATABASE_ROOT;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_USERS;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.UID;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class AuthFunctions {

    public static synchronized void trySignInWithCredential(Activity activity, PhoneAuthCredential credential){
        AUTH.signInWithCredential(credential).addOnCompleteListener(authResultTask -> {
            //Если верификация пользователя прошла успешно
            if (authResultTask.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference(NODE_USERS).orderByChild(CHILD_PHONE)
                            .equalTo(AUTH.getCurrentUser().getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue() == null){
                                //новый пользователь
                                //registerNewUser(activity);
                                RegisterActivity.iNewUser.getUserPersonalData();
                            }else{
                                //старый пользователь
                                if (authResultTask.isSuccessful()) {
                                    Log.w("tag", "СТАРЫЙ ПОЛЬЗОВАТЕЛЬ!!!!");
                                    RegisterActivity.welcomeFunc(activity);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
            } else {
                //Если в верификации произошла ошибка
                Toaster.error(activity, authResultTask.getException().toString()).show();
            }
        });
    }
    // оправка пользователю SMS сообщения с кодом
    public static synchronized void authorizationUser(String mPhoneNumber, long I, TimeUnit timeUnit,
                                       Activity activity, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback) {

        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions
                        .newBuilder(FirebaseAuth.getInstance())
                        .setActivity(activity)
                        .setPhoneNumber(mPhoneNumber)
                        .setTimeout(I, timeUnit)
                        .setCallbacks(mCallback)
                        .build()
        );
    }

    // загружаем из базы даный актуальное состояние данного пользователя
    public static synchronized void downloadUser(IAuthUser iAuthUser){
        DATABASE_ROOT.child(NODE_USERS).child(UID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //TODO:
                USER = snapshot.getValue(User.class);
                USER.setId(snapshot.getKey());
                if (USER != null)
                    iAuthUser.onEndDownloadUser();
                else
                    downloadUser(iAuthUser);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){}
        });
    }
}
