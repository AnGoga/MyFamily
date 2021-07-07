package com.angogasapps.myfamily.ui.screens.family_clock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.getSystemService
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.models.family_clock.ClockObject


class ClockWakeUpperManager private constructor(){

    companion object{
        private var manager: ClockWakeUpperManager? = null
        @Synchronized
        fun getInstance(): ClockWakeUpperManager {
            if (manager == null)
                manager = ClockWakeUpperManager()
            return manager!!
        }
    }

    @Synchronized
    fun wakeUpClock(obj: ClockObject){
        val context: Context = AppApplication.getInstance()
        val intent = Intent(context, FamilyClockReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.RTC_WAKEUP, obj.time] = pendingIntent
    }
}