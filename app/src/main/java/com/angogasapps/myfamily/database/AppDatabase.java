package com.angogasapps.myfamily.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TransactionUser.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao getTransactionUserDao();
}
