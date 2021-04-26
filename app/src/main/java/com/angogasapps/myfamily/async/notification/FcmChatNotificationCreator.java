package com.angogasapps.myfamily.async.notification;

import android.content.Context;
import android.util.Log;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.models.Message;

import org.json.JSONObject;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_IMAGE_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_TEXT_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_VOICE_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class FcmChatNotificationCreator {
    
    public static JSONObject fromChatMessage(Message message) {
        return fromChatMessage(message, USER.getFamily());
    }

    public static JSONObject fromChatMessage(Message message, String to) {
        JSONObject notifObject = new JSONObject();
        try {
            notifObject = buildFrom(message, to);
        } catch (Exception e) {
            Log.e("tag", "ERROR on build FcmMessage");
        }
        return notifObject;
    }


    private static JSONObject buildFrom(Message message, String to) {
        FcmMessageBuilder builder = new FcmMessageBuilder();

        builder.setTo(to);
        builder.setTitle(USER.getName());

        builder.setBody(getTextToChatMessageNotification(message));

        if (message.getType().equals(TYPE_IMAGE_MESSAGE)){
            builder.setImage(message.getValue());
        }

        return builder.build();
    }

    private static String getTextToChatMessageNotification(Message message){
        Context context = AppApplication.getInstance().getApplicationContext();
        StringBuilder string = new StringBuilder();

        if (message.getType().equals(TYPE_TEXT_MESSAGE))
            string.append(message.getValue());
        else if (message.getType().equals(TYPE_IMAGE_MESSAGE))
            string.append("\uD83D\uDCF7" + " ").append(context.getString(R.string.photo));
        else if (message.getType().equals(TYPE_VOICE_MESSAGE))
            string.append("\uD83C\uDFA4" + " ").append(context.getString(R.string.voice));

        context = null;

        return string.toString();
    }

}
/*
JSONObject mainObj = new JSONObject();
        JSONObject messageObj = new JSONObject();
        JSONObject notificationObj = new JSONObject();
        JSONObject dataObj = new JSONObject();

        notificationObj.put(CHILD_TITLE, StringFormater.convertStringToUTF8(USER.getName()));
        if (message.getType().equals(TYPE_TEXT_MESSAGE)) {
            notificationObj.put(CHILD_BODY, StringFormater.convertStringToUTF8(message.getValue()));
        }else if (message.getType().equals(TYPE_VOICE_MESSAGE)){
            // do . . .
        }else if (message.getType().equals(TYPE_IMAGE_MESSAGE)){
            notificationObj.put(CHILD_IMAGE, message.getValue());
        }


        dataObj.put(CHILD_TYPE, message.getType());

        messageObj.put(CHILD_TOPIC, to);
        messageObj.put(NODE_NOTIFICATION, notificationObj);
        messageObj.put(NODE_DATA, dataObj);

        mainObj.put("message", messageObj);
        return mainObj;
 */
