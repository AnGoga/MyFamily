package com.angogasapps.myfamily.async.notification;

import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.objects.User;

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

}
