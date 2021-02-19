package com.angogasapps.myfamily.database;



import android.content.Context;

import androidx.room.Room;

import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.async.LoadFamilyThread;
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts;
import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.objects.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;

public class DatabaseManager {
    public static volatile boolean usersLoadIsEnd = false;
    public static volatile boolean messagesLoadIsEnd = false;

    private static volatile ArrayList<Message> messageList = new ArrayList<>();
    private static volatile ArrayList<User> userList = new ArrayList<>();


    public static final String DATABASE_NAME = "database";

    private static volatile AppDatabase database;


    public static void init(Context context) {
        database = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
    }

    public static AppDatabase getDatabase() {
        return database;
    }

    public static void loadUsersAndMessages() {
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
            }
        };

        Observable.create(emitter -> {
            UserDao userDao = DatabaseManager.getDatabase().getTransactionUserDao();
            List<TransactionUser> cashList = userDao.getAll();
            for (TransactionUser user : cashList) {
                userList.add(new User(user));
            }
            usersLoadIsEnd = true;

            searchNewUsers();

        }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(observer);

        Observable.create(emitter -> {
            MessageDao messageDao = DatabaseManager.getDatabase().getMessageDao();
            messageList = new ArrayList(messageDao.getAll());
            messagesLoadIsEnd = true;

        }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(observer);
    }


    public static ArrayList<Message> getMessageList() {
        return messageList;
    }

    public static ArrayList<User> getUserList() {
        return userList;
    }

    private static void searchNewUsers() {

        if (!AppApplication.isOnline() && !LoadFamilyThread.isEnd) {
//            if (AUTH.getCurrentUser() == null) {
//                return;
//            }else{
//                for (User user : userList)
//                    if (user.getId().equals(AUTH.getCurrentUser().getUid()))
//                        FirebaseVarsAndConsts.USER = new User(user);
//            }
            return;
        }else if (AppApplication.isOnline()) {
            while (!LoadFamilyThread.isEnd) {
            }
        }

        for (User user : FirebaseVarsAndConsts.familyMembersMap.values()) {
            if (!userList.contains(user))
                DatabaseManager.getDatabase().getTransactionUserDao().insert(user);
        }

    }
}
