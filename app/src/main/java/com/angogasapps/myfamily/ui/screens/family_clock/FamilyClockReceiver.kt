package com.angogasapps.myfamily.ui.screens.family_clock

import android.R
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.angogasapps.myfamily.app.AppNotificationManager.CHANNEL_ID


class FamilyClockReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

//        val intent = Intent(context, AlarmClockActivity::class.java)
//                .also { it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); }
//
//        context.startActivity(intent)

        val fullScreenIntent = Intent(context, AlarmClockActivity::class.java)
        val fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.sym_def_app_icon)
                .setContentTitle("Incoming call")
                .setContentText("(919) 555-1234")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setAutoCancel(false)


        NotificationManagerCompat.from(context).notify(115, builder.build())


        /*var alert: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        var r = RingtoneManager.getRingtone(context, alert)

        if (r == null) {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            r = RingtoneManager.getRingtone(context, alert)
            if (r == null) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                r = RingtoneManager.getRingtone(context, alert)
            }
        }
        r?.play()*/

    }
}

/*
        AppNotificationManager.createNotificationChanel(context)

        val builder: NotificationCompat.Builder = Builder(context)
                .setSmallIcon(R.drawable.btn_star)
                .setContentTitle("This is title of notification")
                .setContentText("This is a notification Text")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.sym_def_app_icon))
//                .setAutoCancel(true)

        val intent = Intent(context, AlarmClockActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 113, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        builder.setContentIntent(pendingIntent)

        builder.setFullScreenIntent(pendingIntent, true)


        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(115, builder.build())
*/
