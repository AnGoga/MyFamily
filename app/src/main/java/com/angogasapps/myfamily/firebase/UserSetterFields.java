package com.angogasapps.myfamily.firebase;


import android.net.Uri;
import android.util.Log;

import com.angogasapps.myfamily.firebase.interfaces.IOnEndSetUserField;
import com.angogasapps.myfamily.objects.User;

import java.util.Objects;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_FAMILY;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_PHOTO_URL;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DEFAULT_URL;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_USERS;

public class UserSetterFields {

    public static synchronized <T> void setField(String userId, String fieldName, T value,
                                                 IOnEndSetUserField iOnEndSetUserField){
         DATABASE_ROOT.child(NODE_USERS).child(userId).child(fieldName).setValue(value)
                 .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        iOnEndSetUserField.onSuccessEnd();
                    }else{
                        Log.e("tag", Objects.requireNonNull(task.getResult()).toString());
                        iOnEndSetUserField.onFailureEnd();
                    }
                 });
    }
    public static synchronized void setUserPhoto(User user, Uri photoUri, IOnEndSetUserField iOnEndSetUserField){
        if (photoUri != Uri.EMPTY){
            RegisterUserFunks.loadUserPhotoToStorage(photoUri, iOnEndSetUserField);
        }else{
            setField(user.getId(), CHILD_PHOTO_URL, DEFAULT_URL, iOnEndSetUserField);
        }
    }

    public static synchronized void setPhotoURL(User user, Uri photoUri, IOnEndSetUserField iOnEndSetUserField){
        UserSetterFields.setUserPhoto(user, photoUri, iOnEndSetUserField);
    }

    public static synchronized void setFamily(User user, String family, IOnEndSetUserField iOnEndSetUserField) {
        UserSetterFields.setField(user.getId(), CHILD_FAMILY, family, new IOnEndSetUserField() {
            @Override
            public void onSuccessEnd() {
                user.setFamily(family);
                iOnEndSetUserField.onSuccessEnd();
            }
            @Override
            public void onFailureEnd() {
                iOnEndSetUserField.onFailureEnd();
            }
        });

    }
}
