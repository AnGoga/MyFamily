package com.angogasapps.myfamily.ui.screens.news_center;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.angogasapps.myfamily.firebase.NewsCenterFunks;
import com.angogasapps.myfamily.models.events.NewsEvent;
import com.angogasapps.myfamily.models.events.NewsObject;
import com.angogasapps.myfamily.utils.NewsUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import io.reactivex.subjects.PublishSubject;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_NEWS;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class NewsManager {
    private static NewsManager newsManager;

    private PublishSubject<NewsEvent> subject = PublishSubject.create();

    private ChildEventListener listener;

    private volatile ArrayList<NewsObject> newsList = new ArrayList<>();

    {
        initManager();
    }

    public static NewsManager getInstance(){
        synchronized (NewsManager.class){
            if (newsManager == null)
                newsManager = new NewsManager();
            return newsManager;
        }
    }

    public PublishSubject<NewsEvent> subject(){
        return this.subject;
    }

    private void initManager() {
        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NewsManager.this.onChildAdded(snapshot);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                NewsManager.this.onChildRemoved(snapshot);
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        DATABASE_ROOT.child(NODE_NEWS).child(USER.getFamily()).addChildEventListener(listener);
    }

    private synchronized void onChildAdded(DataSnapshot snapshot){
        NewsObject object = NewsObject.from(snapshot);
        if (NewsObject.isCanLife(object)) {
            newsList.add(object);

            NewsEvent event = new NewsEvent();
            event.setEvent(NewsEvent.ENewsEvents.added);
            event.setNewsId(object.getId());
            event.setIndex(newsList.size() - 1);
            subject.onNext(event);
        }else{
            NewsCenterFunks.deleteNewsObject(object);
        }
    }

    private synchronized void onChildRemoved(DataSnapshot snapshot){
        NewsObject object = NewsObject.from(snapshot);
        int index = NewsUtils.getIndexOfDeleteNews(newsList, object);
        if (index == -1) return;
        newsList.remove(index);

        NewsEvent event = new NewsEvent();
        event.setIndex(index);
        event.setNewsId(object.getId());
        event.setEvent(NewsEvent.ENewsEvents.removed);
        subject.onNext(event);
    }

    public ArrayList<NewsObject> getAllNews(){
        return this.newsList;
    }

}
