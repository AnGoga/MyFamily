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
        return fromChatMessage(message, USER.getFamily() + "-chat");
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
