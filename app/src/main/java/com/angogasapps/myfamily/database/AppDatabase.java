package com.angogasapps.myfamily.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.angogasapps.myfamily.models.Message;
import com.angogasapps.myfamily.models.User;

@Database(entities = {User.class, Message.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
    public abstract MessageDao getMessageDao();
}
