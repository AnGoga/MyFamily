package com.angogasapps.myfamily.firebase;


import android.net.Uri;
import android.util.Log;

import com.angogasapps.myfamily.firebase.interfaces.IOnEndSetUserField;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_PHOTO;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.DEFAULT_URL;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.NODE_USERS;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.UID;

public class UserSetterFields {

    public static synchronized <T> void setField(String fieldName, T value,
                                                 IOnEndSetUserField iOnEndSetUserField){
         DATABASE_ROOT.child(NODE_USERS).child(AUTH.getCurrentUser().getUid()).child(fieldName).setValue(value)
                 .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        iOnEndSetUserField.onSuccessEnd();
                    }else{
                        Log.e("tag", Objects.requireNonNull(task.getResult()).toString());
                        iOnEndSetUserField.onFailureEnd();
                    }
                 });
    }
    public static synchronized void setUserPhoto(Uri photoUri, IOnEndSetUserField iOnEndSetUserField){
        if (photoUri != Uri.EMPTY){
            RegisterUserFunks.loadUserPhotoToStorage(photoUri, iOnEndSetUserField);
        }else{
            setField(CHILD_PHOTO, DEFAULT_URL, iOnEndSetUserField);
        }
    }
}
