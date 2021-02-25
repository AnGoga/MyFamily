package com.angogasapps.myfamily.async;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.angogasapps.myfamily.app.AppNotificationManager;
import com.angogasapps.myfamily.database.DatabaseManager;
import com.angogasapps.myfamily.objects.ChatChildEventListener;
import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.objects.MessageNotification;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

import static com.angogasapps.myfamily.async.MessageNotificationManager.*;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_CHAT;

public class TestChatService extends FirebaseMessagingService {
    @Override
    public void onCreate() {
        AppNotificationManager.createNotificationChanel(this);
        Log.d("TAG", "TestChatService стартовал");
    }

    @Override
    public void onNewToken(@NonNull String s) {

    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("TAG", "TestChatService умер");
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.i("TAG", "Сообщение получено");

        HashMap<String, String> map = new HashMap<>(remoteMessage.getData());

        if (map.size() == 0)
            return;
        if (map.get(PARAM_TYPE).equals(TYPE_MESSAGE)){
            Message message = new Message();
            message.setType(map.get(PARAM_MESSAGE_TYPE));
            message.setFrom(map.get(PARAM_FROM_ID));
            message.setValue(map.get(PARAM_VALUE));

            Notification notification = MessageNotification.build(this, message, map.get(PARAM_FROM_NAME));

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(1, notification);
        }
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
