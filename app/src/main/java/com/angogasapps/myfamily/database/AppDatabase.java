package com.angogasapps.myfamily.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.angogasapps.myfamily.objects.Message;

@Database(entities = {TransactionUser.class, Message.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao getTransactionUserDao();
    public abstract MessageDao getMessageDao();
}
