package com.angogasapps.myfamily.objects;


import android.net.Uri;

import com.angogasapps.myfamily.firebase.UserSetterFields;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndSetUserField;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_BIRTHDAY;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_FAMILY;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_NAME;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.UID;

public class User {
    private String id = "";
    private String phone = "";
    private String family = "";
    private String name = "";
    private Long birthday = 0L;
    private String photoURL = "";

    public User(){
        this.id = "";
        this.phone = "";
        this.family = "";
        this.name = "";
        this.birthday = 0L;
        this.photoURL = "";
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPhone() {
        return phone;
    }
    public String getPhotoURL() {
        return photoURL;
    }
    public Long getBirthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return "id = " + UID + "\nphone = " + this.phone + "\nfamily = " + this.family +
                "\nname = " + this.name + "\nbirthday = " + this.birthday;
    }

    public String getFamily() {
        return family;
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
