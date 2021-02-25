package com.angogasapps.myfamily.firebase;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import com.angogasapps.myfamily.async.MessageNotificationManager;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase;
import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.objects.MessageNotification;
import com.angogasapps.myfamily.utils.StringFormater;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.getMessageKey;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_FROM;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_TIME;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_TYPE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_VALUE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.FOLDER_IMAGE_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.FOLDER_VOICE_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_CHAT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.STORAGE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_IMAGE_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_TEXT_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_VOICE_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.UID;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.chatImageMessangesMap;

public class ChatFunks {
    public static void sendMessage(String type, String value) {
        sendMessageWithKey(type, value, getMessageKey());
    }
    public static void sendMessageWithKey(String type, String value, String key){
        DatabaseReference path = DATABASE_ROOT.child(NODE_CHAT).child(USER.getFamily());

        if (type.equals(TYPE_TEXT_MESSAGE)) {
            value = StringFormater.formatStringToSend(value);
        }

        HashMap<String, Object> messageMap = new HashMap<>();
        messageMap.put(CHILD_FROM, UID);
        messageMap.put(CHILD_TYPE, type);
        messageMap.put(CHILD_VALUE, value);
        messageMap.put(CHILD_TIME, ServerValue.TIMESTAMP);

        String finalValue = value;
        path.child(key).updateChildren(messageMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Message message = new Message();
                message.setFrom(UID);
                message.setType(type);
                message.setValue(finalValue);
                MessageNotificationManager.getInstance().sendNotificationMessage(message, USER);

            }
        });
    }

    public static void sendImage(Uri imageUri) {
        String key = getMessageKey();
        StorageReference path = STORAGE_ROOT.child(FOLDER_IMAGE_MESSAGE).child(USER.getFamily()).child(key);


        path.putFile(imageUri).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                path.getDownloadUrl().addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        String url = task2.getResult().toString();
                        sendMessageWithKey(TYPE_IMAGE_MESSAGE, url, key);
                    }
                });
            }
        });
    }
    public static void sendVoice(File voiceFile, String key){
        StorageReference path = STORAGE_ROOT.child(FOLDER_VOICE_MESSAGE).child(USER.getFamily()).child(key);

        path.putFile(Uri.fromFile(voiceFile)).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()){
                path.getDownloadUrl().addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()){
                        String url = task2.getResult().toString();
                        sendMessageWithKey(TYPE_VOICE_MESSAGE, url, key);
                    }
                });
            }
//            voiceFile.delete();
        });
    }

/*
    public static void downloadImageMessageAndSetBitmap(String path, String key, ImageView imageView, Activity activity) {
        if (chatImageMessangesMap.containsKey(key)){
//            activity.runOnUiThread(() -> {
                imageView.setImageBitmap(chatImageMessangesMap.get(key));
//            });
        }else {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        URL photoUrl = new URL(path);
                        InputStream downloadStream = photoUrl.openStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(downloadStream);

                        chatImageMessangesMap.put(key, bitmap);

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
*/

    public static void downloadImageMessageAndSetBitmap(String path, String key, ImageView imageView){
        if (chatImageMessangesMap.containsKey(key)){
            imageView.setImageBitmap(chatImageMessangesMap.get(key));
        }else{
            Observable.create(emitter -> {
                URL photoUrl = new URL(path);
                InputStream downloadStream = photoUrl.openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(downloadStream);
                chatImageMessangesMap.put(key, bitmap);
                emitter.onNext(bitmap);
            })
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Object>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {}
                @Override
                public void onNext(@NonNull Object o) {
                    imageView.setImageBitmap(chatImageMessangesMap.get(key));
                }
                @Override
                public void onError(@NonNull Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onComplete() { }
            });
        }
    }

    public static void getFileFromStorage(File file, String url, IOnEndCommunicationWithFirebase i){
        STORAGE_ROOT.getStorage().getReference(url).getFile(file).addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               i.onSuccess();
           }else{
               i.onFailure();
           }
        });
    }

    public static void getVoiceFileFromStorage(File file, String key, IOnEndCommunicationWithFirebase i){
        STORAGE_ROOT.child(FOLDER_VOICE_MESSAGE).child(USER.getFamily()).child(key).getFile(file).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                i.onSuccess();
            else
                i.onFailure();
        });
    }
}
