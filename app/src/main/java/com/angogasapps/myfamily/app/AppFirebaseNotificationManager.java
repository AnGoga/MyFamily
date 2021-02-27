package com.angogasapps.myfamily.app;

import android.util.Log;

import com.angogasapps.myfamily.objects.User;
import com.google.firebase.messaging.FirebaseMessaging;

public class AppFirebaseNotificationManager {

//    public static void subscribeToFamily(User user){
//        if(user == null || user.getFamily().equals(""))
//            return;
//
//        FirebaseMessaging.getInstance().subscribeToTopic(user.getFamily()).addOnCompleteListener(task -> {
//            if (!task.isSuccessful()){
//                task.getException().printStackTrace();
//                return;
//            }
//            Log.i("TAG", "subscribeToFamily: is successful");
//        });
//    }
}
