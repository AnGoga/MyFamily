package com.angogasapps.myfamily.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts;

import java.util.ArrayList;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.ROLE_MEMBER;

import kotlin.jvm.JvmField;
import kotlin.jvm.JvmStatic;

public class Family {
    private static Family family;

    private ArrayList<User> usersList = new ArrayList<>();

    public static Family getInstance(){
        synchronized (Family.class){
            if (family == null)
                family = new Family();
            return family;
        }
    }

    public String getMemberNameById(String id){
        for (User user : Family.getInstance().getUsersList()){
            if (user.getId().equals(id)){
                return user.getName();
            }

        }
        return id;
    }

    public String getMemberRoleById(String id){
        for (User user : Family.getInstance().getUsersList()){
            if (user.getId().equals(id)){
                return user.getRole();
            }
        }
        return ROLE_MEMBER;
    }

    public boolean containsUserWithId(String id){
        boolean isContains = false;
        for (User user : usersList){
            if (user.getId().equals(id)) {
                isContains = true;
                break;
            }
        }
        return isContains;
    }

    @Nullable
    public User getUserById(String id){
        for (User user : usersList){
            if (user.getId().equals(id)){
                return user;
            }
        }
        return null;
    }

    public String getNameByPhone(String phone){
        for (User user : usersList){
            if (user.getPhone().equals(phone)){
                return user.getName();
            }
        }
        return phone;
    }

    public static String getPreferUserName(String phone){
        SharedPreferences sf = AppApplication.getInstance().getSharedPreferences("phone-name", Context.MODE_PRIVATE);
        String name = sf.getString(phone, phone);
        return name;
    }

    public static Bitmap getMemberImageById(String id){
        User user = Family.getInstance().getUserById(id);
        if (user != null) return user.getUserPhoto();
        return User.default_user_photo;
    }

    @Nullable
    public User getUserByPhone(String phone){
        for (User user : usersList){
            if (user.getId().equals(phone)){
                return user;
            }
        }
        return null;
    }

    public Bitmap getMemberImageByPhone(String phone){
        User user = Family.getInstance().getUserByPhone(phone);
        if (user != null)
            return user.getUserPhoto();
        return User.default_user_photo;
    }

    public ArrayList<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(ArrayList<User> usersList) {
        this.usersList = usersList;
    }
}
