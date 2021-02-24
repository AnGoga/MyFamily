package com.angogasapps.myfamily.objects;


import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.angogasapps.myfamily.firebase.UserSetterFields;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndSetUserField;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_FAMILY;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.UID;


@Entity
public class User {
    @PrimaryKey
    @NonNull
    protected String id = " ";
    protected String phone = "";
    protected String family = "";
    protected String name = "";
    protected Long birthday = 0L;
    protected String photoURL = "";
    protected String role = "";
    @Ignore
    protected Bitmap userPhoto;

    public User(){}

    public User(User user){
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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof User))
            return false;
        return this.id.equals(((User)obj).getId());
    }

    public boolean haveFamily(){
        return !this.getFamily().equals("");
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getRole() {
        return role;
    }

    public Bitmap getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Bitmap userPhoto) {
        this.userPhoto = userPhoto;
    }



}
