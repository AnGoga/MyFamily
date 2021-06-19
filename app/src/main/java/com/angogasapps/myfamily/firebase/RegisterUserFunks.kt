package com.angogasapps.myfamily.firebase;

import android.app.Activity;
import android.net.Uri;

import com.angogasapps.myfamily.firebase.interfaces.IOnEndSetUserField;
import com.angogasapps.myfamily.ui.screens.registeractivity.RegisterActivity;

import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_BIRTHDAY;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_FAMILY;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_NAME;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_PHONE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_PHOTO_URL;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DEFAULT_URL;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.FOLDER_USERS_PHOTOS;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_USERS;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.STORAGE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class RegisterUserFunks {

    public static synchronized void registerNewUser(Activity activity, String userName, Long userBirthdayTimeMillis, Uri photoUri){
        // создаём словарь по шаблону класса User

        IOnEndSetUserField i = new IOnEndSetUserField() {
            @Override
            public void onSuccessEnd() {
                String uid = AUTH.getCurrentUser().getUid();
                HashMap<String, Object> userAttrMap = new HashMap<>();
                if (photoUri == null) userAttrMap.put(CHILD_PHOTO_URL, DEFAULT_URL);
                userAttrMap.put(CHILD_PHONE, AUTH.getCurrentUser().getPhoneNumber());
                userAttrMap.put(CHILD_FAMILY, "");
                userAttrMap.put(CHILD_NAME, userName);
                userAttrMap.put(CHILD_BIRTHDAY, userBirthdayTimeMillis);
                //userAttrMap.put(CHILD_PHOTO, photoUri);
                DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(userAttrMap)
                        .addOnCompleteListener(task -> {

                            if (task.isSuccessful()){
                                RegisterActivity.welcomeFunc(activity);
                            }else {
                                Toasty.error(activity, task.getException().getMessage()).show();
                            }
                        });
            }

            @Override
            public void onFailureEnd() {}
        };

        if (photoUri != null){
            UserSetterFields.setPhotoURL(USER, photoUri, i);
        }else{
            i.onSuccessEnd();
        }

    }
    public static synchronized void loadUserPhotoToStorage(Uri photoUri, IOnEndSetUserField iOnEndSetUserField){
        //TODO:
        StorageReference path = STORAGE_ROOT.child(FOLDER_USERS_PHOTOS).child(AUTH.getCurrentUser().getUid());
        path.putFile(photoUri).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()){
                path.getDownloadUrl().addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()){
                        String photoLink = task2.getResult().toString();
                        UserSetterFields.setField(AUTH.getCurrentUser().getUid(), CHILD_PHOTO_URL, photoLink, iOnEndSetUserField);
                    }else {
                        iOnEndSetUserField.onFailureEnd();
                    }
                });
            }else{
                iOnEndSetUserField.onFailureEnd();
            }
        });
    }
}
