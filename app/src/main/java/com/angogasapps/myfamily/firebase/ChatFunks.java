package com.angogasapps.myfamily.firebase;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_FROM;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_TIME;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_TYPE;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_VALUE;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.NODE_CHAT;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.UID;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.USER;

public class ChatFunks {
    public static void sendMessage(String type, String value){
        DatabaseReference path = DATABASE_ROOT.child(NODE_CHAT).child(USER.getFamily());

        HashMap messageMap = new HashMap<String, Object>();
        messageMap.put(CHILD_FROM, UID);
        messageMap.put(CHILD_TYPE, type);
        messageMap.put(CHILD_VALUE, value);
//        TODO:
        messageMap.put(CHILD_TIME, ServerValue.TIMESTAMP);

        String key = path.push().getKey();
        path.child(key).updateChildren(messageMap).addOnCompleteListener(task -> {
           if (task.isSuccessful()){

           }else{

           }
        });
    }

    public static void sendImage(Uri imageUri){





    }
}
