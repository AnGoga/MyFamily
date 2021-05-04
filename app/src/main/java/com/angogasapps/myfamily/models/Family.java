package com.angogasapps.myfamily.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts;

import java.util.ArrayList;

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

    public ArrayList<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(ArrayList<User> usersList) {
        this.usersList = usersList;
    }
}
