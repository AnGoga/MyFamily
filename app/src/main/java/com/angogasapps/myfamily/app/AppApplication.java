package com.angogasapps.myfamily.app;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class AppApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        AppApplication.context = this.getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}