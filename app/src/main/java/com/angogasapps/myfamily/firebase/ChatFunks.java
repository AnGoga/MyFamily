package com.angogasapps.myfamily.firebase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import com.angogasapps.myfamily.utils.StringFormater;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_FROM;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_TIME;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_TYPE;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_VALUE;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.FOLDER_IMAGE_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.NODE_CHAT;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.STORAGE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.TYPE_IMAGE_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.TYPE_TEXT_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.UID;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.USER;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.familyMembersImagesMap;

public class ChatFunks {
    public static void sendMessage(String type, String value) {
        DatabaseReference path = DATABASE_ROOT.child(NODE_CHAT).child(USER.getFamily());

        if (type == TYPE_TEXT_MESSAGE) {
            value = StringFormater.formatStringToSend(value);
        }

        HashMap<String, Object> messageMap = new HashMap<>();
        messageMap.put(CHILD_FROM, UID);
        messageMap.put(CHILD_TYPE, type);
        messageMap.put(CHILD_VALUE, value);
        messageMap.put(CHILD_TIME, ServerValue.TIMESTAMP);

        String key = path.push().getKey();
        path.child(key).updateChildren(messageMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
            }
        });
    }

    public static void sendImage(Uri imageUri) {
        StorageReference path = STORAGE_ROOT.child(FOLDER_IMAGE_MESSAGE).child(USER.getFamily());

        path.putFile(imageUri).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                path.getDownloadUrl().addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        String url = task2.getResult().toString();
                        sendMessage(TYPE_IMAGE_MESSAGE, url);
                    }
                });
            }
        });
    }
    public static void sendVoice(File voiceFile){

    }

    public static void downloadImageAndSetBitmap(String path, ImageView imageView, Activity activity) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    URL photoUrl = new URL(path);
                    InputStream downloadStream = photoUrl.openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(downloadStream);

                    activity.runOnUiThread(() -> {
                        imageView.setImageBitmap(bitmap);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
