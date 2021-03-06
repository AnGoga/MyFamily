package com.angogasapps.myfamily.async.notification;

import android.util.Log;


import com.angogasapps.myfamily.utils.StringFormater;

import org.json.JSONObject;

import java.util.HashMap;

public class FcmMessageBuilder {

    private static final String NODE_DATA = "data";
    private static final String NODE_NOTIFICATION = "notification";
    public static final String CHILD_TOPIC = "topic";
    private static final String CHILD_TITLE = "title";
    private static final String CHILD_BODY = "body";
    public static final String CHILD_MESSAGE = "message";

    private static final String CHILD_IMAGE = "image";


    private String to = "";
    private String title = "";
    private String body = "";
    private String image = "";
    private HashMap<String, String> data = new HashMap<>();

    public FcmMessageBuilder setTo(String to) {
        this.to = to;
        return this;
    }

    public FcmMessageBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public FcmMessageBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public FcmMessageBuilder setData(HashMap<String, String> data) {
        this.data = data;
        return this;
    }

    public FcmMessageBuilder setImage(String imageUrl) {
        this.image = imageUrl;
        return this;
    }


    public JSONObject build() {
        return buildMessage();
    }

    private JSONObject buildMessage() {
        try {
            JSONObject mainObj = new JSONObject();
            JSONObject messageObj = new JSONObject();
            JSONObject notificationObj = new JSONObject();
            JSONObject dataObj = new JSONObject();

            notificationObj.put(CHILD_TITLE, StringFormater.convertStringToUTF8(this.title));
            notificationObj.put(CHILD_BODY, StringFormater.convertStringToUTF8(this.body));
            notificationObj.put(CHILD_IMAGE, this.image);

            for (String key : this.data.keySet())
                dataObj.put(key, this.data.get(key));

            messageObj.put(CHILD_TOPIC, this.to);
            messageObj.put(NODE_NOTIFICATION, notificationObj);
            messageObj.put(NODE_DATA, dataObj);

            mainObj.put(CHILD_MESSAGE, messageObj);
            return mainObj;
        } catch (Exception e) {
            Log.e("tag", "ERROR on build FcmMessage");
            return null;
        }
    }
}
