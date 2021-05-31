package com.angogasapps.myfamily.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.angogasapps.myfamily.models.DairyObject;
import com.angogasapps.myfamily.models.Message;
import com.angogasapps.myfamily.models.User;

@Database(entities = {User.class, Message.class, DairyObject.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
    public abstract MessageDao getMessageDao();
    public abstract DairyDao getDairyDao();
}
