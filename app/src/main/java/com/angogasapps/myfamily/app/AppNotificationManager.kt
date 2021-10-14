package com.angogasapps.myfamily.app

import android.os.Build
import com.angogasapps.myfamily.R
import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Context
import com.angogasapps.myfamily.app.AppNotificationManager

object AppNotificationManager {
    const val CHANNEL_ID = "app_channel_id"

    fun createNotificationChanel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = context.getString(R.string.channel_name)
            val description = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = context.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}