package com.angogasapps.myfamily.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts;

public class WithUsers {
    public static String getMemberNameById(String id){
        try {
            return FirebaseVarsAndConsts.familyMembersMap.get(id).getName();
        }catch (NullPointerException e){
            return id;
        }
    }

    public static Bitmap getMemberImageById(String id, Context context){
//        Bitmap image = FirebaseVarsAndConsts.familyMembersImagesMap.get(id);
        Bitmap image = FirebaseVarsAndConsts.familyMembersMap.get(id).getUserPhoto();
        if (image == null){
            image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_default_user_photo);
        }

        return image;
    }

    public static String getMemberPhoneById(String id){
        try{
            return FirebaseVarsAndConsts.familyMembersMap.get(id).getPhone();
        }catch (NullPointerException e){
            return "";
        }
    }
}
