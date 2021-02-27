package com.angogasapps.myfamily.async.notification;

import android.app.Notification;
import android.util.Log;

import com.angogasapps.myfamily.database.DatabaseManager;
import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.objects.User;
import com.angogasapps.myfamily.utils.Others;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_TEXT_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class MessageNotificationManager {
    private static MessageNotificationManager manager;

    private APIService apiService = Client.getInstance("https://fcm.googleapis.com/").create(APIService.class);

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
//        HashMap<String, String> map = new HashMap<>();
//        map.put(PARAM_FROM_ID, message.getFrom());
//        map.put(PARAM_FROM_NAME, fromUser.getName());
//        map.put(PARAM_TYPE, TYPE_MESSAGE);
//        map.put(PARAM_MESSAGE_TYPE, message.getType());
//        if (message.getType().equals(TYPE_TEXT_MESSAGE))
//            map.put(PARAM_VALUE, message.getValue());
//        else
//            map.put(PARAM_VALUE, "");
//
//
//        RemoteMessage notifMessage = new RemoteMessage.Builder(fromUser.getFamily())
//                .setMessageId(message.getId())
//                .setMessageType(message.getType())
//                .setData(map)
//                .build();
//
//        FirebaseMessaging.getInstance().send(notifMessage);
//        Log.i("TAG", "Сообщение отправлено");

        Others.runInNewThread(() -> {
            ArrayList<User> users = new ArrayList<>(DatabaseManager.getDatabase().getUserDao().getAll());
            for (User user : users){
                if (!user.getId().equals(USER.getId()))
                    testSendNotification(user.getToken(), "Message", message.getValue());
            }
        });

    }

    public void testSendNotification(String userToken, String title, String message){
        NotificationSender.Data data = new NotificationSender.Data(title, message);
        NotificationSender sender = new NotificationSender(data, userToken);
        apiService.sendNotification(sender).enqueue(new Callback<APIService.MyResponse>() {
            @Override
            public void onResponse(Call<APIService.MyResponse> call, Response<APIService.MyResponse> response) {
                if (response.code() == 200)
                    if (response.body().success != 1) {
                        Log.e("TAG", "в отправке Notification произошла ошибка");
                    }else{
                        Log.i("TAG", "Notification успешно отправлено");
                    }
            }

            @Override
            public void onFailure(Call<APIService.MyResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
