package com.angogasapps.myfamily.async.notification;

import android.content.Context;
import android.util.Log;


import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts;
import com.angogasapps.myfamily.objects.Message;
import com.google.gson.internal.$Gson$Preconditions;

import org.json.JSONObject;

import java.util.HashMap;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_IMAGE_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_TEXT_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_VOICE_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class FcmMessage {


    private static final String NODE_DATA = "data";
    private static final String NODE_NOTIFICATION = "notification";
    private static final String CHILD_TOPIC = "topic";
    private static final String CHILD_TITLE = "title";
    private static final String CHILD_BODY = "body";

    private JSONObject message;

    public JSONObject getMessage(){
        return message;
    }

    private FcmMessage(JSONObject message){
        this.message = message;
    }

    public static class Builder{
        private String to = "";
        private String title = "";
        private String body = "";
        private String image = "";
        private HashMap<String, String> data = new HashMap<>();

        public Builder setTo(String to) {
            this.to = to;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public Builder setData(HashMap<String, String> data) {
            this.data = data;
            return this;
        }

        public Builder setImage(String imageUrl){
            this.image = imageUrl;
            return this;
        }

        public FcmMessage build(){
            return new FcmMessage(buildMessage());
        }
        private JSONObject buildMessage(){
            try {
                JSONObject mainObj = new JSONObject();
                JSONObject messageObj = new JSONObject();
                JSONObject notificationObj = new JSONObject();
                JSONObject dataObj = new JSONObject();

                notificationObj.put(CHILD_TITLE, this.title);
                notificationObj.put(CHILD_BODY, this.body);

                for (String key : this.data.keySet())
                    dataObj.put(key, this.data.get(key));

                messageObj.put(CHILD_TOPIC, this.to);
                messageObj.put(NODE_NOTIFICATION, notificationObj);
                messageObj.put(NODE_DATA, dataObj);

                mainObj.put("message", messageObj);
                return mainObj;
            }catch (Exception e){
                Log.e("tag", "ERROR on build FcmMessage");
                return null;
            }
        }
    }

    public static JSONObject buildMessageObj(Message message, String to){
        FcmMessage.Builder builder = new FcmMessage.Builder().setTo(to);
        builder.setTitle(USER.getName()/*FirebaseVarsAndConsts.familyMembersMap.get(message.getFrom()).getName()*/);
        builder.setBody(getTextToMessageNotification(message));

        if (message.getType().equals(TYPE_IMAGE_MESSAGE)){
            builder.setImage(message.getValue());
        }

        return builder.build().getMessage();
    }

    private static String getTextToMessageNotification(Message message){
        Context context = AppApplication.getInstance().getApplicationContext();
        StringBuilder string = new StringBuilder();


        if (message.getType().equals(TYPE_TEXT_MESSAGE))
            string.append(message.getValue());
        else if (message.getType().equals(TYPE_IMAGE_MESSAGE))
            string.append("\uD83D\uDCF7" + " ").append(context.getString(R.string.photo));
        else if (message.getType().equals(TYPE_VOICE_MESSAGE))
            string.append("\uD83C\uDFA4" + " ").append(context.getString(R.string.voice));

        return string.toString();
    }
}
