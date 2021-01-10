package com.angogasapps.myfamily.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.annotation.NonNull;


import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts;
import com.angogasapps.myfamily.objects.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_MEMBERS;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DEFAULT_URL;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.FOLDER_FAMILY_EMBLEMS;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_FAMILIES;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_USERS;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.STORAGE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;
//import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.familyMembersImagesMap;
//import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.familyMembersRolesMap;


public class LoadFamilyThread extends AsyncTask<User, Integer, ArrayList<User>> {
    Context context;
    volatile ArrayList<String> familyMembersId = new ArrayList<>();
    volatile ArrayList<User> familyMembersList = new ArrayList<>();


    public LoadFamilyThread(Context context) {
        this.context = context;
    }


    @Override
    protected ArrayList<User> doInBackground(User... users) {
        DATABASE_ROOT.child(NODE_FAMILIES).child(USER.getFamily()).child(CHILD_MEMBERS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            familyMembersId.add(dataSnapshot.getKey());
                            String string = dataSnapshot.getValue(String.class);
//                            familyMembersRolesMap.put(dataSnapshot.getKey(), string);
                        }
                        downloadFamilyMembers(0);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        return null;
    }

    private void downloadFamilyMembers(int pos) {
        String id = familyMembersId.get(pos);
        DATABASE_ROOT.child(NODE_USERS).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                familyMembersList.add(snapshot.getValue(User.class));
                if (pos < familyMembersId.size() - 1)
                    downloadFamilyMembers(pos + 1);
                else
                    downloadImages();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        

    }
    private void downloadImages() {
        MemberImagesDownloaderThread downloadImageThread = new MemberImagesDownloaderThread();

        downloadImageThread.start();
        try {
            downloadImageThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initUsersCollection();
    }

    private void initUsersCollection(){
        for (User member : familyMembersList) {
            FirebaseVarsAndConsts.familyMembersMap.put(member.getId(), member);
        }
    }


    class MemberImagesDownloaderThread extends Thread{
        @Override
        public void run() {
            super.run();
            for (int i = 0; i < familyMembersList.size(); i++) {
                if (familyMembersList.get(i).getPhotoURL().equals(DEFAULT_URL)) {
//                    familyMembersImagesMap.put(familyMembersList.get(i).getId(), null);
                } else {
                    try {
                        URL photoUrl = new URL(familyMembersList.get(i).getPhotoURL());
                        InputStream downloadStream = photoUrl.openStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(downloadStream);
//                        familyMembersImagesMap.put(familyMembersList.get(i).getId(), bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
