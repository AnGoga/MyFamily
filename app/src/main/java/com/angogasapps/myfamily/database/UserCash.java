package com.angogasapps.myfamily.database;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserCash {
    @PrimaryKey
    private String id = "";
    private String phone = "";
    private String family = "";
    private String name = "";
    private Long birthday = 0L;
    private String photoURL = "";
    private String role = "";
    private Bitmap userPhoto;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public void setRole(String role) {
        this.role = role;
    }

    public Bitmap getUserPhoto() {
        return userPhoto;
    }
    public void setUserPhoto(Bitmap userPhoto) {
        this.userPhoto = userPhoto;
    }
}
