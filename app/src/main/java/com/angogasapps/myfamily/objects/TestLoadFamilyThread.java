package com.angogasapps.myfamily.objects;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_EMBLEM;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_MEMBERS;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_NAME;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DATABASE_ROOT;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DEFAULT_URL;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_FAMILIES;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_USERS;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.familyMembersMap;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.familyMembersRolesMap;


public final class TestLoadFamilyThread extends AsyncTask<User, Integer, ArrayList<User>> {
    Context context;
    volatile ArrayList<String> familyMembersId = new ArrayList<>();
    volatile ArrayList<User> familyMembersList = new ArrayList<>();
    volatile HashMap<String, String> usersRoleMap = new HashMap<>();
    volatile String familyEmblemURL;
    volatile Bitmap familyEmblem;
    volatile String familyName;

    volatile ExecutorService pull;


    public TestLoadFamilyThread(Context context) {
        this.context = context;

    }

    @Override
    protected ArrayList<User> doInBackground(User... users) {
        try {
            FamilyDownloaderThread familyDownloaderThread = new FamilyDownloaderThread();
            FamilyEmblemDownloaderThread emblemDownloaderThread = new FamilyEmblemDownloaderThread();

            familyDownloaderThread.start();
            familyDownloaderThread.join();
            System.out.println("Конец ожидания");

            downloadMembers();

            downloadMembersPhoto();

            emblemDownloaderThread.start();

            initConnections();

            if (emblemDownloaderThread.isAlive())
                emblemDownloaderThread.join();

            Log.i("tag", "Загрузка завершена");

        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, User> aStringUserHashMap = familyMembersMap;
        return null;
    }

    private void downloadMembers(){
        pull = Executors.newFixedThreadPool(2);
        for (int i = 0; i < familyMembersId.size(); i++) {
            pull.submit(new MemberDownloaderThread(familyMembersId.get(i)));
        }
        pull.shutdown();

    }

    private void downloadMembersPhoto(){
        pull = Executors.newFixedThreadPool(2);
        for (int i = 0; i < familyMembersList.size(); i++) {
            pull.submit(new PhotoDownloaderThread(familyMembersList.get(i).getPhotoURL(), i));
        }
        pull.shutdown();
    }

    private Bitmap downloadBitmapByURL(String url){
        try{
            URL photoUrl = new URL(url);
            InputStream downloadStream = photoUrl.openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(downloadStream);
            return bitmap;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private class FamilyDownloaderThread extends Thread {
        boolean isDownload = false;
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
                            System.out.println("Конец потока");

                            isDownload = true;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
            while (!isDownload){
                continue;
            }
        }
    }

    private class MemberDownloaderThread extends Thread{
        String memberId;
        boolean isDownload = false;

        public MemberDownloaderThread(String id){
            this.memberId = id;
        }

        @Override
        public void run() {
            DATABASE_ROOT.child(NODE_USERS).child(memberId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    familyMembersList.add(snapshot.getValue(User.class));
                    isDownload = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
            while (!isDownload){
                continue;
            }
        }
    }

    private class PhotoDownloaderThread extends Thread{
        String photoUrl;
        int pos;

        public PhotoDownloaderThread(String photoUrl, int pos) {
            this.photoUrl = photoUrl;
            this.pos = pos;
        }

        @Override
        public void run() {
            familyMembersList.get(pos).setBitmap(downloadBitmapByURL(this.photoUrl));
        }
    }

    private class FamilyEmblemDownloaderThread extends Thread{
        @Override
        public void run() {
            familyEmblem = downloadBitmapByURL(familyEmblemURL);
        }
    }

    private void initConnections(){
        for (User user : familyMembersList){
            user.setRole(usersRoleMap.get(user.getId()));
            familyMembersMap.put(user.getId(), user);
        }
    }
}
