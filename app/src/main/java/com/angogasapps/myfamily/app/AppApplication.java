package com.angogasapps.myfamily.app;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.angogasapps.myfamily.database.DatabaseManager;


public class AppApplication extends Application {
    private static AppApplication app;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        DatabaseManager.init(getApplicationContext());
        AppNotificationManager.createNotificationChanel(getApplicationContext());
    }


    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) app.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static AppApplication getInstance(){
        return app;
    }

}