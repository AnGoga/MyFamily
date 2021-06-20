package com.angogasapps.myfamily.database;



import android.content.Context;

import androidx.room.Room;

import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.models.Message;
import com.angogasapps.myfamily.models.User;
import com.angogasapps.myfamily.utils.Async;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DatabaseManager {
    public static volatile boolean usersLoadIsEnd = false;

    private static volatile ArrayList<User> userList = new ArrayList<>();


    public static final String DATABASE_NAME = "database";

    private static volatile AppDatabase database;


    private static void init(Context context) {
        database = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
    }

    public static AppDatabase getInstance() {
        synchronized (AppDatabase.class) {
            if (database == null)
                init(AppApplication.getInstance().getApplicationContext());
            return database;
        }
    }

    public static void comeInByDatabase(IOnEnd onEnd){
        Async.runInNewThread(() -> {
           UserDao dao = getInstance().getUserDao();
           userList = new ArrayList<>(dao.getAll());
           onEnd.onEnd();
        });
    }



    public static ArrayList<User> getUserList() {
        return userList;
    }

    public interface IOnEnd{
        void onEnd();
    }

    public static void updateInfoForUsers(ArrayList<User> users){
        Async.runInNewThread(() -> {
            UserDao dao = getInstance().getUserDao();
            for (User user : users) {
                dao.insert(user);
            }
        });
    }
}
