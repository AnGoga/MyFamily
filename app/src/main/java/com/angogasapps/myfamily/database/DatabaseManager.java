package com.angogasapps.myfamily.database;



import android.content.Context;

import androidx.room.Room;

public class DatabaseManager {

    public static final String DATABASE_NAME = "database";

    private static volatile AppDatabase database;


    public static void init(Context context) {
        database = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
    }

    public static AppDatabase getDatabase() {
        return database;
    }
}
