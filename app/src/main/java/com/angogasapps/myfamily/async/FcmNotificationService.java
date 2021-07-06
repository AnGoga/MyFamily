package com.angogasapps.myfamily.async;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.angogasapps.myfamily.app.AppNotificationManager;
import com.angogasapps.myfamily.async.notification.TokensManager;
import com.angogasapps.myfamily.utils.Async;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class FcmNotificationService extends FirebaseMessagingService {
    @Override
    public void onCreate() {
        AppNotificationManager.createNotificationChanel(this);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        USER.setToken(token);
        Async.runInNewThread(() -> {
            while(!LoadFamilyThread.isEnd){  }
            TokensManager.getInstance().updateToken(token);
        });
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("TAG", "FcmNotificationService умер");
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.i("TAG", "Сообщение-уведомление получено -> " + remoteMessage.toString());

    }
}
