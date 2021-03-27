package com.angogasapps.myfamily.async;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppNotificationManager;
import com.angogasapps.myfamily.async.notification.TokensManager;
import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.objects.MessageNotification;
import com.angogasapps.myfamily.ui.screens.splash.SplashActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

import static com.angogasapps.myfamily.async.notification.MessageNotificationManager.*;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class FcmNotificationService extends FirebaseMessagingService {
    @Override
    public void onCreate() {
        AppNotificationManager.createNotificationChanel(this);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        USER.setToken(token);
        TokensManager.getInstance().updateToken(token, USER);
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
