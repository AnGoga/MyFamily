package com.angogasapps.myfamily.async

import android.content.Intent
import android.util.Log
import com.angogasapps.myfamily.app.AppNotificationManager
import com.angogasapps.myfamily.async.notification.TokensManager
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts
import com.angogasapps.myfamily.models.family_clock.ClockObject
import com.angogasapps.myfamily.ui.screens.family_clock.ClockWakeUpperManager.Companion.getInstance
import com.angogasapps.myfamily.utils.Async
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*
import kotlin.collections.ArrayList

class FcmNotificationService : FirebaseMessagingService() {
    override fun onCreate() {
        AppNotificationManager.createNotificationChanel(this)
    }

    override fun onNewToken(token: String) {
        FirebaseVarsAndConsts.USER.token = token
        Async.runInNewThread {
            while (!LoadFamilyThread.isEnd) {}
            TokensManager.updateToken(token)
        }
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        Log.d("TAG", "FcmNotificationService умер")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i("TAG", "Сообщение-уведомление получено -> $remoteMessage")
        val map = remoteMessage.data
        if (map.isNotEmpty()) {
            analyzeData(HashMap<String, String>(map))
        }
    }

    private fun analyzeData(map: HashMap<String, String>) {
        val type = map[FirebaseVarsAndConsts.CHILD_TYPE] ?: return
        if (type == "") return
        if (type == FirebaseVarsAndConsts.TYPE_ALARM_CLOCK_MESSAGE) {
            onGetAlarmClockMessage(map)
        }
    }

    private fun onGetAlarmClockMessage(map: HashMap<String, String>) {
        val id = map[FirebaseVarsAndConsts.CHILD_ID]?:""
        val time = (map[FirebaseVarsAndConsts.CHILD_TIME]?:"0").toLong()
        val fromId = map[FirebaseVarsAndConsts.CHILD_FROM_ID]?:""
        val fromPhone = map[FirebaseVarsAndConsts.CHILD_FROM_PHONE]?:""
        val obj = ClockObject(id, ArrayList(), fromPhone, fromId, time, null)

        getInstance().wakeUpClock(obj)
    }
}