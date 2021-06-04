package com.angogasapps.myfamily.firebase;

import android.net.Uri;

import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase;
import com.angogasapps.myfamily.models.events.NewsObject;
import com.angogasapps.myfamily.utils.NewsUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_NEWS;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.STORAGE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class NewsCenterFunks {
    public static synchronized void createNewTextNews(String newsText, IOnEndCommunicationWithFirebase i){
        NewsObject newsObject = new NewsObject();
        newsObject.setType(NewsObject.TYPE_TEXT);
        newsObject.setValue(newsText);
        newsObject.setFromPhone(USER.getPhone());

        DatabaseReference ref = DATABASE_ROOT.child(NODE_NEWS).child(USER.getFamily());
        String key = ref.push().getKey();
        sendNewNews(newsObject, key, i);
    }

    public static synchronized void createNewImageNews(Uri uri, IOnEndCommunicationWithFirebase i){
        DatabaseReference ref = DATABASE_ROOT.child(NODE_NEWS).child(USER.getFamily());
        String key = ref.push().getKey();

        StorageReference path = STORAGE_ROOT.child(NODE_NEWS).child(USER.getFamily()).child(key);

        path.putFile(uri).addOnCompleteListener(task1 -> {
           if (task1.isSuccessful()){
               path.getDownloadUrl().addOnCompleteListener(task2 -> {
                   if (task2.isSuccessful()){
                       String url = task2.getResult().toString();
                       NewsObject newsObject = new NewsObject();
                       newsObject.setValue(url);
                       newsObject.setFromPhone(USER.getPhone());
                       newsObject.setType(NewsObject.TYPE_IMAGE);
                       sendNewNews(newsObject, key, i);
                   }else{
                       task2.getException().toString();
                       i.onFailure();
                   }
               });
           }else{
               task1.getException().printStackTrace();
               i.onFailure();
           }
        });
    }

    private static synchronized void sendNewNews(NewsObject newsObject, String key, IOnEndCommunicationWithFirebase i){
        DATABASE_ROOT.child(NODE_NEWS).child(USER.getFamily()).child(key)
                .updateChildren(NewsUtils.getMap(newsObject)).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                i.onSuccess();
            }else{
                task.getException().printStackTrace();
                i.onFailure();
            }
        });
    }

    public static synchronized void deleteNewsObject(NewsObject object){
        DATABASE_ROOT.child(NODE_NEWS).child(USER.getFamily()).child(object.getId()).removeValue();
    }
}
