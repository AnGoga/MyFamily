package com.angogasapps.myfamily.async.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.util.Log;

import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.models.Message;
import com.angogasapps.myfamily.utils.Async;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.firebase.messaging.FirebaseMessaging;


import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class FcmMessageManager {
    private static final String postUrl = "https://fcm.googleapis.com/v1/projects/myfamily-1601b/messages:send";
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };


    public static void sendChatNotificationMessage(Message message){

        Async.runInNewThread(() -> {
            try {
//                JSONObject messageObject = FcmMessage.buildMessageObj(message, to);
                JSONObject messageObject = FcmChatNotificationCreator.fromChatMessage(message);
                Log.d("TAG", "sendMessage: \n" + messageObject);
                sendNotificationMessage(messageObject);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }


    public static void sendNotificationMessage(JSONObject fcmMessage) throws IOException {
        HttpURLConnection connection = getConnection();
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(fcmMessage.toString());
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            String response = inputstreamToString(connection.getInputStream());
            System.out.println("Message успешно отправлено в Firebase");
            System.out.println(response);
        } else {
            System.out.println("При отправке message в Firebase произошла ошибка");
            String response = inputstreamToString(connection.getErrorStream());
            System.out.println(response);
        }
    }

    private static HttpURLConnection getConnection() throws IOException {
        URL url = new URL(postUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
        return httpURLConnection;

    }

    private static String getAccessToken() throws IOException{
        AssetManager assetManager = AppApplication.getInstance().getAssets();

        GoogleCredential googleCredential = GoogleCredential
                .fromStream(assetManager.open("service-account.json"))
                .createScoped(Arrays.asList(SCOPES));
        googleCredential.refreshToken();
        return googleCredential.getAccessToken();

    }

    private static String inputstreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

    public static void subscribeToFamilyChat(){
        if (canSubscribeToFamily()) {
            FirebaseMessaging.getInstance().subscribeToTopic(USER.getFamily() + "-chat").addOnCompleteListener(task -> {
                Log.d("TAG", "subscribeToFamily: " + task.toString());
            });
        }
    }

    public static void unsubscribeFromFamilyChat(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(USER.getFamily() + "-chat").addOnCompleteListener(task -> {
            Log.d("TAG", "unsubscribeFromFamily: " + task.toString());
        });
    }

    private static boolean canSubscribeToFamily(){
        SharedPreferences sf = AppApplication.getInstance().getSharedPreferences("family-chat", Context.MODE_PRIVATE);
        return sf.getBoolean("can_send_notification", true);
    }

    public static void setPermissionToGetChatNotifications(boolean isHavePermission){
        SharedPreferences sf = AppApplication.getInstance().getSharedPreferences("family-chat", Context.MODE_PRIVATE);
        Editor editor = sf.edit();
        editor.putBoolean("can_send_notification", isHavePermission);
        editor.apply();
        if (isHavePermission){
            subscribeToFamilyChat();
        }else{
            unsubscribeFromFamilyChat();
        }
    }

}
