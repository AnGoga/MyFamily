package com.angogasapps.myfamily.ui.screens.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.database.DatabaseManager;
import com.angogasapps.myfamily.database.MessageDao;
import com.angogasapps.myfamily.models.Message;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class ChatActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private ChatFragment chatFragment;
    private volatile Subject<Message> subject;
    private MessageDao messageDao;
    private volatile ArrayList<Message> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        initDatabase();
        loadMessages();
        prepareSubject();
        initFragment();

    }


    private void loadMessages(){
        /*Async.runInNewThread(() -> {
            messageList = new ArrayList<>(messageDao.getAll());
            Log.d(TAG, "loadMessages: ");
        });
        */
    }

    private void saveNewMessage(Message message){

        if (!DatabaseManager.getMessagesList().contains(message)){
            DatabaseManager.getMessagesList().add(message);
            messageDao.insert(message);
        }


    }

    public void prepareSubject() {

        Observer<Message> observer = new Observer<Message>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {}

            @Override
            public void onNext(@NonNull Message message) {
                saveNewMessage(message);
            }

            @Override
            public void onError(@NonNull Throwable e) {}

            @Override
            public void onComplete() {}
        };


        this.subject = PublishSubject.create();
        subject.subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(observer);

    }

    private void initFragment(){
        chatFragment = new ChatFragment();

        chatFragment.setSubject(this.subject);


        getSupportFragmentManager().beginTransaction()
                .add(R.id.chatActivityDataContainer, chatFragment).commit();
    }

    private void initDatabase(){
        messageDao = DatabaseManager.getInstance().getMessageDao();
    }
}