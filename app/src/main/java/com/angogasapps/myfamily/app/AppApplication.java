package com.angogasapps.myfamily.app;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.angogasapps.myfamily.database.DatabaseManager;


public class AppApplication extends Application {
    private static Context context;
    private static boolean inChat = false;
    @Override
    public void onCreate() {
        super.onCreate();
        AppApplication.context = this.getApplicationContext();
        DatabaseManager.init(context);
        AppNotificationManager.createNotificationChanel(context);
    }


    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static boolean isInChat(){
        return inChat;
    }

    public static void setExitChatStatus(){
        inChat = false;
    }

    public static void setReturnToChatStatus(){
        inChat = true;
    }



}