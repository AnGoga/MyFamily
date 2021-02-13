package com.angogasapps.myfamily.objects;


import android.graphics.Bitmap;
import android.net.Uri;

import com.angogasapps.myfamily.database.TransactionUser;
import com.angogasapps.myfamily.firebase.UserSetterFields;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndSetUserField;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_BIRTHDAY;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_FAMILY;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_NAME;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.UID;

public class User extends TransactionUser {
    public User(){
        super();
    }

    public User(User user){
        super(user);
    }

    public User(TransactionUser user){
        this.id = user.getId();
        this.phone = user.getPhone();
        this.family = user.getFamily();
        this.name = user.getName();
        this.birthday = user.getBirthday();
        this.photoURL = user.getPhotoURL();
        this.role = user.getRole();
        this.userPhoto = user.getUserPhoto();
    }


    public void setBitmap(Bitmap bitmap){
        this.userPhoto = bitmap;
    }
    public void setRole(String role){
        this.role = role;
    }
    public void setId(String id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "id = " + UID + "\nphone = " + this.phone + "\nfamily = " + this.family +
                "\nname = " + this.name + "\nbirthday = " + this.birthday;
    }

    public synchronized void setFamily(String family, IOnEndSetUserField iOnEndSetUserField) {
        UserSetterFields.setField(CHILD_FAMILY, family, new IOnEndSetUserField() {
            @Override
            public void onSuccessEnd() {
                User.this.family = family;
                iOnEndSetUserField.onSuccessEnd();
            }
            @Override
            public void onFailureEnd() {
                iOnEndSetUserField.onFailureEnd();
            }
        });

    }

    public synchronized void setName(String name, IOnEndSetUserField iOnEndSetUserField) {
        UserSetterFields.setField(CHILD_NAME, name, new IOnEndSetUserField() {
            @Override
            public void onSuccessEnd() {
                User.this.name = name;
                iOnEndSetUserField.onSuccessEnd();
            }

            @Override
            public void onFailureEnd() {
                iOnEndSetUserField.onFailureEnd();
            }
        });
        this.name = name;
    }

    public synchronized void setBirthday(Long birthday, IOnEndSetUserField iOnEndSetUserField) {
        UserSetterFields.setField(CHILD_BIRTHDAY, birthday, new IOnEndSetUserField() {
            @Override
            public void onSuccessEnd() {
                User.this.birthday = birthday;
                iOnEndSetUserField.onSuccessEnd();
            }

            @Override
            public void onFailureEnd() {
                iOnEndSetUserField.onSuccessEnd();
            }
        });
    }

    public synchronized void setPhotoURL(Uri photoUri, IOnEndSetUserField iOnEndSetUserField){
        UserSetterFields.setUserPhoto(photoUri, iOnEndSetUserField);
    }


}
