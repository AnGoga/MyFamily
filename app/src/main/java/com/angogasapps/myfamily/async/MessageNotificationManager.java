package com.angogasapps.myfamily.async;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts;
import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.objects.User;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.RemoteMessageCreator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.FIREBASE_URL;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_TEXT_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class MessageNotificationManager {
    private static MessageNotificationManager manager;

    public static String PARAM_VALUE = "value";
    public static String PARAM_FROM_NAME = "from_name";
    public static String PARAM_FROM_ID = "from_id";
    public static String PARAM_TYPE = "type";
    public static String TYPE_MESSAGE = "message";
    public static String PARAM_MESSAGE_TYPE = "message_type";


    public static MessageNotificationManager getInstance(){
        if (manager == null)
            manager = new MessageNotificationManager();
        return manager;
    }


    public void sendNotificationMessage(Message message, User fromUser){
        HashMap<String, String> map = new HashMap<>();
        map.put(PARAM_FROM_ID, message.getFrom());
        map.put(PARAM_FROM_NAME, fromUser.getName());
        map.put(PARAM_TYPE, TYPE_MESSAGE);
        map.put(PARAM_MESSAGE_TYPE, message.getType());
        if (message.getType().equals(TYPE_TEXT_MESSAGE))
            map.put(PARAM_VALUE, message.getValue());
        else
            map.put(PARAM_VALUE, "");


        RemoteMessage notifMessage = new RemoteMessage.Builder(fromUser.getFamily())
                .setMessageId(message.getId())
                .setMessageType(message.getType())
                .setData(map)
                .build();

        FirebaseMessaging.getInstance().send(notifMessage);
        Log.i("TAG", "Сообщение отправлено");

    }
}
