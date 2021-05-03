package com.angogasapps.myfamily.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts;
import com.angogasapps.myfamily.models.Family;
import com.angogasapps.myfamily.models.User;
import com.google.firebase.database.DataSnapshot;

public class WithUsers {

    public static Bitmap getMemberImageById(String id, Context context){
        Bitmap image = null;
        try {
            image = Family.getInstance().getUserById(id).getUserPhoto();
            if (image == null) {
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_default_user_photo);
            }
        }catch (Exception e){}
        return image;
    }

}
