package com.angogasapps.myfamily.async;

import android.content.Context;
import android.content.Intent;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class ServiceManager {
    public static void checkServices(Context context){
//        if (!ChatService.isRunning(context) && USER.haveFamily()){
//            Intent intent = new Intent(context, ChatService.class);
//            intent.putExtra(ChatService.PARAM_USER_ID, USER.getId());
//            intent.putExtra(ChatService.PARAM_FAMILY_ID, USER.getFamily());
//
//            context.startService(intent);
//        }

//        if (!FcmNotificationService.isRunning(context) && USER.haveFamily())
//            context.startService(new Intent(context, FcmNotificationService.class));
    }
}
