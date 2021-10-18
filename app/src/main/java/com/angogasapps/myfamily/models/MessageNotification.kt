package com.angogasapps.myfamily.models

import android.app.Notification
import com.angogasapps.myfamily.database.UserDao
import com.angogasapps.myfamily.app.AppNotificationManager
import com.angogasapps.myfamily.R
import android.content.Intent
import com.angogasapps.myfamily.ui.screens.chat.ChatActivity
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.angogasapps.myfamily.firebase.*
import java.lang.StringBuilder

object MessageNotification {
    fun build(context: Context, message: Message, userDao: UserDao): Notification {
        return createNotification(context, message, userDao)
    }

    fun build(context: Context, message: Message, userName: String): Notification {
        return createNotification(context, message, userName)
    }

    private fun createNotification(
        context: Context,
        message: Message,
        userDao: UserDao
    ): Notification {
        val user = userDao.getById(message.from)
        return createNotification(context, message, if (user == null) "___" else user.name)
    }

    private fun createNotification(
        context: Context,
        message: Message,
        userName: String
    ): Notification {
        val builder = NotificationCompat.Builder(context, AppNotificationManager.CHANNEL_ID)
            .setContentTitle(context.getString(R.string.my_family))
            .setContentText(getTextToMessageNotification(context, message, userName))
            .setSmallIcon(R.drawable.default_user_photo)
        val intent = Intent(context, ChatActivity::class.java)
        val pIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOngoing(false)
        return builder.build()
    }

    private fun getTextToMessageNotification(
        context: Context,
        message: Message,
        userName: String
    ): String {
        val string = StringBuilder()
        string.append(userName)
        string.append(": ")
        when (message.type) {
            TYPE_TEXT_MESSAGE -> string.append(message.value)
            TYPE_IMAGE_MESSAGE -> string.append("\uD83D\uDCF7" + " ")
                .append(context.getString(R.string.photo))
            TYPE_VOICE_MESSAGE -> string.append(
                "\uD83C\uDFA4" + " "
            ).append(context.getString(R.string.voice))
        }
        return string.toString()
    }
}