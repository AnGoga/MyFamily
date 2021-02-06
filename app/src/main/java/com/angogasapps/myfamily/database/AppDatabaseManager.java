package com.angogasapps.myfamily.database;



import android.content.Context;

import androidx.room.Room;

public class AppDatabaseManager {

    private static volatile AppDatabase database;


    public static void init(Context context) {
        database = Room.databaseBuilder(context, AppDatabase.class, "database").build();
    }

    public static AppDatabase getDatabase() {
        return database;
    }
}
