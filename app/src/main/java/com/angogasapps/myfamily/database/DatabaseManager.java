package com.angogasapps.myfamily.database;



import android.content.Context;

import androidx.room.Room;

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
    public static volatile boolean messagesLoadIsEnd = false;

    private static volatile ArrayList<Message> messagesList = new ArrayList<>();
    private static volatile ArrayList<User> userList = new ArrayList<>();


    public static final String DATABASE_NAME = "database";

    private static volatile AppDatabase database;


    public static void init(Context context) {
        database = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
    }

    public static AppDatabase getDatabase() {
        return database;
    }

    public static void loadUsersAndMessages(IOnEnd iOnEnd) {
        Observer<Object> observer = new Observer<Object>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull Object o) {
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }

            @Override
            public void onComplete() {

                if (usersLoadIsEnd && messagesLoadIsEnd){
                    iOnEnd.onEnd();
                }
            }
        };

        Observable.create(emitter -> {
            UserDao userDao = DatabaseManager.getDatabase().getUserDao();
            userList = new ArrayList<>(userDao.getAll());
            usersLoadIsEnd = true;
            emitter.onComplete();
        }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(observer);

        Observable.create(emitter -> {
            MessageDao messageDao = DatabaseManager.getDatabase().getMessageDao();
            messagesList = new ArrayList(messageDao.getAll());
            messagesLoadIsEnd = true;
            emitter.onComplete();

        }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(observer);
    }


    public static ArrayList<Message> getMessagesList() {
        return messagesList;
    }

    public static ArrayList<User> getUserList() {
        return userList;
    }

    public interface IOnEnd{
        void onEnd();
    }

    public static void searchNewUsers(ArrayList<User> users){
        Async.runInNewThread(() -> {
            ArrayList<User> cashUserList = new ArrayList<>(database.getUserDao().getAll());
            for (User user : users)
                if (!cashUserList.contains(user))
                    database.getUserDao().insert(user);
        });
    }
}
