package com.angogasapps.myfamily.utils;


import android.Manifest;
import android.app.Activity;

import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permissions {

    public static final String AUDIO_RECORD_PERM = Manifest.permission.RECORD_AUDIO;

    public static final int PERMISSION_CALLBACK = 12345;

    public static boolean havePermission(String permission, Activity activity){
        if (Build.VERSION.SDK_INT >= 23
                && ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){

            //get this permission
            ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_CALLBACK);

            return false;
        }else{
            return true;
        }
    }
}
