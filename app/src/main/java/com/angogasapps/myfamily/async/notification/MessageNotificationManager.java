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


    public void sendNotificationMessage(Message message, User fromUser){
//        FcmNotificationSender sender = new FcmNotificationSender(
//                "/eyJhbGciOiJSUzI1NiIsImtpZCI6IjBlYmMyZmI5N2QyNWE1MmQ5MjJhOGRkNTRiZmQ4MzhhOTk4MjE2MmIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vbXlmYW1pbHktMTYwMWIiLCJhdWQiOiJteWZhbWlseS0xNjAxYiIsImF1dGhfdGltZSI6MTYxNDI3MzUxMSwidXNlcl9pZCI6IkJVUGZUeVc2bXZXUFUxQ0c3SVdJaHJzaWd4ejIiLCJzdWIiOiJCVVBmVHlXNm12V1BVMUNHN0lXSWhyc2lneHoyIiwiaWF0IjoxNjE0NDQyODg5LCJleHAiOjE2MTQ0NDY0ODksInBob25lX251bWJlciI6IisxMjM0NTY3ODkxMCIsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsicGhvbmUiOlsiKzEyMzQ1Njc4OTEwIl19LCJzaWduX2luX3Byb3ZpZGVyIjoicGhvbmUifX0.qFrQCU08NxnlIUpv3k0GSxh0gRvj3hdCiTineW8v8PNWsXgcZe3FeaA8aFnwaKTKeIloLm2Kz25ugriH1tUDkeZiEu_M4ev3WeuQtkZN8Kqp78m7O5fGHj3-7Z4qLXzcqbGyKO4-LghAYkwFN4W09_hn9UgTPnOcxXI_rBnF3D6H6h5ceA4TT16vimg7thYoVACkiFBCTh6lIsd5ffKHFlb_isW_59RRfInEYA6Cx42tjpouYsfCDOC1sPkP7tBiKD7cAp1eezSDQ5mocY6qPgz2rMJWd7dwALfWYT1x7zvShXKN-yDkWgfQ7gwD8C5jxKItUeF61T0fPyScxy8-ug",
//                "Сообщение",
//                message.getValue(),
//                AppApplication.getInstance().getApplicationContext()
//        );
//        sender.sendNotifications();
    }

}
