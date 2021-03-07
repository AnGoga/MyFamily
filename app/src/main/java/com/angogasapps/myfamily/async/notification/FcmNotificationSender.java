package com.angogasapps.myfamily.async.notification;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.angogasapps.myfamily.ui.screens.chat.ChatActivity.TAG;

public class FcmNotificationSender {

    String userFcmToken;
    String title;
    String body;
    Context mContext;


    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey ="AAAA9csGIhU:APA91bF0U8mRa0KiVjbs3WEURjHb2A8SAUTO3XqegTfTA3egEh1boKbWiuGriAfTnPMQ_qu5EfoulSuQdrrEidUAgBAMh8Sj4ZNjF76-oPUHxm2GQCK16lqcpjSlpCvCgfcy-YzPaHR7";

    public FcmNotificationSender(String userFcmToken, String title, String body, Context mContext) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
    }

    public void sendNotifications() {

        requestQueue = Volley.newRequestQueue(mContext);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("body", body);
//            notiObject.put("icon", "icon"); // enter icon that exists in drawable only



            mainObj.put("notification", notiObject);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, response -> {
                // code run is got response
                Log.e(TAG, "SendNotifications: ВРОДЕ КАК ОТПРАВЛЕНО");
            }, error -> {
                error.printStackTrace();
                Log.e(TAG, "SendNotifications: ОШИБКА");
            }) {
                @Override
                public Map<String, String> getHeaders() {

                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;

                }
            };
            requestQueue.add(request);


        } catch (JSONException e) {
            e.printStackTrace();
        }




    }
}
