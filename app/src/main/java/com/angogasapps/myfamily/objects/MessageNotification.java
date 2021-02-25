package com.angogasapps.myfamily.objects;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.database.UserDao;
import com.angogasapps.myfamily.ui.screens.chat.ChatActivity;

import io.reactivex.annotations.NonNull;

import static com.angogasapps.myfamily.app.AppNotificationManager.CHANNEL_ID;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_IMAGE_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_TEXT_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_VOICE_MESSAGE;


public class MessageNotification {

    public static Notification build(Context context, Message message, UserDao userDao){
        return createNotification(context, message, userDao);
    }

    public static Notification build(Context context, Message message, String userName){
        return createNotification(context, message, userName);
    }

    private static Notification createNotification(Context context, Message message, UserDao userDao){
        User user = userDao.getById(message.getFrom());

        return createNotification(context, message, user == null ? "___" : user.getName());
    }

    private static Notification createNotification(Context context, Message message, String userName){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle(context.getString(R.string.my_family))
                        .setContentText(getTextToMessageNotification(context, message, userName))
                        .setSmallIcon(R.drawable.default_user_photo);


        Intent intent = new Intent(context, ChatActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(false);

        return builder.build();

    }

    private static String getTextToMessageNotification(Context context, Message message, String userName){
        StringBuilder string = new StringBuilder();


        string.append(userName);
        string.append(": ");


        if (message.getType().equals(TYPE_TEXT_MESSAGE))
            string.append(message.getValue());
        else if (message.getType().equals(TYPE_IMAGE_MESSAGE))
            string.append("\uD83D\uDCF7" + " ").append(context.getString(R.string.photo));
        else if (message.getType().equals(TYPE_VOICE_MESSAGE))
            string.append("\uD83C\uDFA4" + " ").append(context.getString(R.string.voice));

        return string.toString();
    }

}
