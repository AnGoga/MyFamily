package com.angogasapps.myfamily.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.objects.User;

@Database(entities = {User.class, Message.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
    public abstract MessageDao getMessageDao();
}
