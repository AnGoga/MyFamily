package com.angogasapps.myfamily.firebase;

import android.app.Activity;
import android.net.Uri;

import com.angogasapps.myfamily.firebase.interfaces.IOnEndSetUserField;
import com.angogasapps.myfamily.ui.activities.RegisterActivity;
import com.angogasapps.myfamily.ui.toaster.Toaster;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_BIRTHDAY;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_FAMILY;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_ID;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_NAME;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_PHONE;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_PHOTO;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.FOLDER_USERS_PHOTOS;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.NODE_USERS;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.STORAGE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.UID;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.USER;

public class RegisterUserFunks {

    public static synchronized void registerNewUser(Activity activity, String userName, Long userBirthdayTimeMillis, Uri photoUri){
        // создаём словарь по шаблону класса User
        USER.setPhotoURL(photoUri, new IOnEndSetUserField() {
            @Override
            public void onSuccessEnd() {
                String uid = AUTH.getCurrentUser().getUid();
                HashMap userAttrMap = new HashMap<String, Object>();
                //userAttrMap.put(CHILD_ID, uid);
                userAttrMap.put(CHILD_PHONE, AUTH.getCurrentUser().getPhoneNumber());
                userAttrMap.put(CHILD_FAMILY, "");
                userAttrMap.put(CHILD_NAME, userName);
                userAttrMap.put(CHILD_BIRTHDAY, userBirthdayTimeMillis);
                //userAttrMap.put(CHILD_PHOTO, photoUri);
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

            @Override
            public void onFailureEnd() {}
        });

    }
    public static synchronized void loadUserPhotoToStorage(Uri photoUri, IOnEndSetUserField iOnEndSetUserField){
        //TODO:
        StorageReference path = STORAGE_ROOT.child(FOLDER_USERS_PHOTOS).child(AUTH.getCurrentUser().getUid());
        path.putFile(photoUri).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()){
                path.getDownloadUrl().addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()){
                        String photoLink = task2.getResult().toString();
                        UserSetterFields.setField(CHILD_PHOTO, photoLink, iOnEndSetUserField);
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
