package com.angogasapps.myfamily.firebase;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.angogasapps.myfamily.objects.User;
import com.angogasapps.myfamily.ui.activites.RegisterActivity;
import com.angogasapps.myfamily.ui.toaster.Toaster;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import java.util.concurrent.TimeUnit;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_ID;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_PHONE;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.NODE_USERS;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.UID;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.USER;

public class AuthFunctions {

    public static void trySignInWithCredential(Activity activity, PhoneAuthCredential credential){
        AUTH.signInWithCredential(credential).addOnCompleteListener(authResultTask -> {
            //Если верификация пользователя прошла успешно
            if (authResultTask.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference(NODE_USERS).orderByChild(CHILD_PHONE)
                            .equalTo(AUTH.getCurrentUser().getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue() == null){
                                //новый пользователь
                                registerNewUser(activity);
                            }else{
                                //старый пользователь
                                if (authResultTask.isSuccessful()) {
                                    Log.e("tag", "СТАРЫЙ ПОЛЬЗОВАТЕЛЬ!!!!");
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
    public static void authorizationUser(String mPhoneNumber, int I, TimeUnit timeUnit,
                                       Activity activity, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mPhoneNumber,
                I, timeUnit,
                activity,
                mCallback
        );
    }
    // регистрация нового пользователя
    public static void registerNewUser(Activity activity){
        // создаём словарь по шаблону класса User
        String uid = AUTH.getCurrentUser().getUid();
        HashMap userAttrMap = new HashMap<String, Object>();
        userAttrMap.put(CHILD_ID, uid);
        userAttrMap.put(CHILD_PHONE, AUTH.getCurrentUser().getPhoneNumber());
        //загружаем даные в баду даных
        DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(userAttrMap)
                .addOnCompleteListener(task -> {
                    // если даные в базу данных успешно добавились
                    if (task.isSuccessful()){
                        RegisterActivity.welcomeFunc(activity);
                    }else {
                        Toaster.error(activity, task.getException().getMessage()).show();
                    }
                });
    }
    // загружаем из базы даный актуальное состояние данного пользователя
    public static void downloadUser(){
        DATABASE_ROOT.child(NODE_USERS).child(UID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                USER = snapshot.getValue(User.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){}
        });
    }
}
