package com.angogasapps.myfamily.async;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;


import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts;
import com.angogasapps.myfamily.objects.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_EMBLEM;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_MEMBERS;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_NAME;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DATABASE_ROOT;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DEFAULT_URL;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_FAMILIES;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_USERS;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.familyMembersMap;
import static com.angogasapps.myfamily.utils.WithUsers.getUserFromSnapshot;


public final class LoadFamilyThread extends AsyncTask<User, Integer, ArrayList<User>> {
    public static volatile boolean isEnd = false;
    Context context;
    volatile ArrayList<String> familyMembersId = new ArrayList<>();
    volatile ArrayList<User> familyMembersList = new ArrayList<>();
    volatile HashMap<String, String> usersRoleMap = new HashMap<>();
    volatile String familyEmblemURL;
    volatile Bitmap familyEmblem;
    volatile String familyName;

    volatile ExecutorService pull;

//    volatile static Lock lock = new ReentrantLock();

    volatile static CountDownLatch lock = new CountDownLatch(1);

    public LoadFamilyThread(Context context) {
        this.context = context;

    }

    @Override
    protected ArrayList<User> doInBackground(User... users) {
        try {
            Log.v("tag", "Thread = " + Thread.currentThread());
            FamilyDownloaderThread familyDownloaderThread = new FamilyDownloaderThread();
            FamilyEmblemDownloaderThread emblemDownloaderThread = new FamilyEmblemDownloaderThread();

            familyDownloaderThread.start();
            familyDownloaderThread.join();

            downloadMembers();

            downloadMembersPhoto();

            ArrayList<User> a = familyMembersList;

            emblemDownloaderThread.start();

            initConnections();

            if (emblemDownloaderThread.isAlive())
                emblemDownloaderThread.join();

            Log.i("tag", "Загрузка завершена");

        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, User> aStringUserHashMap = familyMembersMap;
        isEnd = true;
        context = null;
        return null;
    }

    private void downloadMembers(){
        pull = Executors.newFixedThreadPool(2);
        CountDownLatch monitor = new CountDownLatch(familyMembersId.size());
        for (int i = 0; i < familyMembersId.size(); i++) {
            pull.submit(new MemberDownloaderThread(familyMembersId.get(i), monitor));
        }
        pull.shutdown();
        try {
            monitor.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private void downloadMembersPhoto(){
        pull = Executors.newFixedThreadPool(2);
        CountDownLatch monitor = new CountDownLatch(familyMembersList.size());
        for (int i = 0; i < familyMembersList.size(); i++) {
            pull.submit(new PhotoDownloaderThread(familyMembersList.get(i).getPhotoURL(), i, monitor));
        }
        pull.shutdown();
        try {
            monitor.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Bitmap downloadBitmapByURL(String url){
        try{
            URL photoUrl = new URL(url);
            InputStream downloadStream = photoUrl.openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(downloadStream);
            return bitmap;
        }catch (Exception e) {
            if (url != null)
            if (!url.equals(DEFAULT_URL))
                e.printStackTrace();
        }
        return null;
    }


    private class FamilyDownloaderThread extends Thread {

        @Override
        public void run() {
            DATABASE_ROOT.child(NODE_FAMILIES).child(USER.getFamily())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.child(CHILD_MEMBERS).getChildren()) {
                                familyMembersId.add(dataSnapshot.getKey());
                                String string = dataSnapshot.getValue(String.class);
                                usersRoleMap.put(dataSnapshot.getKey(), string);
                            }
                            familyEmblemURL = snapshot.child(CHILD_EMBLEM).getValue(String.class);
                            familyName = snapshot.child(CHILD_NAME).getValue(String.class);

                            lock.countDown();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
            try {
                lock.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private class MemberDownloaderThread extends Thread{
        String memberId;
        CountDownLatch monitor;


        public MemberDownloaderThread(String id, CountDownLatch monitor){
            this.memberId = id;
            this.monitor = monitor;
        }

        @Override
        public void run() {
            DATABASE_ROOT.child(NODE_USERS).child(memberId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    familyMembersList.add(snapshot.getValue(User.class));
                    familyMembersList.add(getUserFromSnapshot(snapshot));
                    Log.v("tag", "Thread = " + Thread.currentThread());

                    MemberDownloaderThread.this.monitor.countDown();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }

    private class PhotoDownloaderThread extends Thread{
        String photoUrl;
        int pos;
        CountDownLatch monitor;

        public PhotoDownloaderThread(String photoUrl, int pos, CountDownLatch monitor) {
            this.photoUrl = photoUrl;
            this.pos = pos;
            this.monitor = monitor;
        }

        @Override
        public void run() {
            familyMembersList.get(pos).setBitmap(downloadBitmapByURL(this.photoUrl));
            PhotoDownloaderThread.this.monitor.countDown();
        }
    }

    private class FamilyEmblemDownloaderThread extends Thread{
        @Override
        public void run() {
            familyEmblem = downloadBitmapByURL(familyEmblemURL);
        }
    }

    private void initConnections(){
        FirebaseVarsAndConsts.familyEmblemImage = this.familyEmblem;
        for (User user : familyMembersList){
            user.setRole(usersRoleMap.get(user.getId()));
            familyMembersMap.put(user.getId(), user);
        }
    }
}
