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
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.angogasapps.myfamily.ui.screens.chat.ChatActivity.TAG;

public class FcmNotificationSender {

    private String userFcmToken;
    private String title;
    private String body;
    private Context mContext;

    private final String postUrl = "https://fcm.googleapis.com/v1/projects/myfamily-1601b/messages:send";
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };

//    private RequestQueue requestQueue;


    public FcmNotificationSender(String userFcmToken, String title, String body, Context mContext) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
    }

    public void sendMessage(){

    }


    private static String getAccessToken() throws IOException{
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(new FileInputStream("service-account.json"))
                .createScoped(Arrays.asList(SCOPES));
        googleCredential.refreshToken();
        return googleCredential.getAccessToken();
    }
}
