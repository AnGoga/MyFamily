package com.angogasapps.myfamily.async;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.database.DatabaseManager;
import com.angogasapps.myfamily.database.MessageDao;
import com.angogasapps.myfamily.database.UserDao;
import com.angogasapps.myfamily.objects.ChatChildEventListener;
import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.objects.User;
import com.angogasapps.myfamily.ui.screens.chat.ChatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.angogasapps.myfamily.app.AppNotificationManager.CHANNEL_ID;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_CHAT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_IMAGE_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_TEXT_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_VOICE_MESSAGE;

public class ChatService extends Service {
    private final String TAG = "TAG";

    
    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_FAMILY_ID = "family_id";

    private String userId =  "";
    private String familyId = "";

    ChatChildEventListener chatListener;
    MessageDao messageDao;
    UserDao userDao;
    DatabaseReference path;

    public ChatService() {}

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        FirebaseApp.initializeApp(this);
        
        userId = intent.getStringExtra(PARAM_USER_ID);
        familyId = intent.getStringExtra(PARAM_FAMILY_ID);

        messageDao = DatabaseManager.getDatabase().getMessageDao();
        userDao = DatabaseManager.getDatabase().getUserDao();

        chatListener = new ChatChildEventListener(this::onAddMessage);
        path = FirebaseDatabase.getInstance().getReference().child(NODE_CHAT).child(familyId);
        path.addChildEventListener(chatListener);



        return Service.START_STICKY;
//        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        path.removeEventListener(chatListener);
        Log.e(TAG, "onDestroy: ChatService" );
    }

    /*@Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent("com.android.ServiceStopped");
        sendBroadcast(intent);

        super.onTaskRemoved(rootIntent);

    }*/

    public void onAddMessage(Message message){
        if (!AppApplication.isInChat())
        Observable.create(subscriber -> {
            if (!roomHaveItMessage(message)){
                messageDao.insert(message);
                subscriber.onNext(createNewMessageNotification(message));
                subscriber.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}
                    @Override
                    public void onNext(@NonNull Object object) {
                        if (object instanceof Notification) {
                            Notification notification = ((Notification)object);
                            NotificationManager notificationManager =
                                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            Log.d(TAG, "onNext: notification");
                            notificationManager.notify(1, notification);
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {}
                    @Override
                    public void onComplete() {}
                });

    }

    private boolean roomHaveItMessage(Message message){
        return messageDao.getById(message.getId()) != null;
    }

    private Notification createNewMessageNotification(Message message) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.my_family))
                .setContentText(getTextToMessageNotification(message))
                .setSmallIcon(R.drawable.default_user_photo);


        Intent intent = new Intent(this, ChatActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(false);


        return builder.build();
    }

    public static boolean isRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ChatService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private String getTextToMessageNotification(Message message){
        StringBuilder string = new StringBuilder();

        User user = userDao.getById(message.getFrom());
        if (user == null){

        }else{
            string.append(user.getName());
            string.append(": ");
        }

        if (message.getType().equals(TYPE_TEXT_MESSAGE))
            string.append(message.getValue());
        else if (message.getType().equals(TYPE_IMAGE_MESSAGE))
            string.append("\uD83D\uDCF7" + " ").append(getString(R.string.photo));
        else if (message.getType().equals(TYPE_VOICE_MESSAGE))
            string.append("\uD83C\uDFA4" + " ").append(getString(R.string.voice));

        return string.toString();
    }

}