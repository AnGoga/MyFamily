package com.angogasapps.myfamily.async;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppNotificationManager;
import com.angogasapps.myfamily.async.notification.TokensManager;
import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.objects.MessageNotification;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

import static com.angogasapps.myfamily.async.notification.MessageNotificationManager.*;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class TestChatService extends FirebaseMessagingService {
    @Override
    public void onCreate() {
        AppNotificationManager.createNotificationChanel(this);
        Log.d("TAG", "TestChatService стартовал");
    }

    @Override
    public void onNewToken(@NonNull String token) {
        USER.setToken(token);
        TokensManager.getInstance().updateToken(token, USER);
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("TAG", "TestChatService умер");
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.i("TAG", "Сообщение получено");

//        HashMap<String, String> map = new HashMap<>(remoteMessage.getData());
//
//        if (map.size() == 0)
//            return;
//        if (map.get(PARAM_TYPE).equals(TYPE_MESSAGE)){
//            Message message = new Message();
//            message.setType(map.get(PARAM_MESSAGE_TYPE));
//            message.setFrom(map.get(PARAM_FROM_ID));
//            message.setValue(map.get(PARAM_VALUE));
//
//            Notification notification = MessageNotification.build(this, message, map.get(PARAM_FROM_NAME));

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.default_user_photo)
                .setContentTitle(remoteMessage.getData().get("Title"))
                .setContentText(remoteMessage.getData().get("Message")).build();

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(1, notification);
    }


    public static boolean isRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (TestChatService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
