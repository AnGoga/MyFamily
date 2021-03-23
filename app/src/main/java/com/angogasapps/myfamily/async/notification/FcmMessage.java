package com.angogasapps.myfamily.async.notification;

import android.util.Log;


import org.json.JSONObject;

import java.util.HashMap;

public class FcmMessage {
    private static String NODE_DATA = "data";
    private static String NODE_NOTIFICATION = "notification";
    private static String CHILD_TOPIC = "topic";
    private static String CHILD_TITLE = "title";
    private static String CHILD_BODY = "body";


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
}
