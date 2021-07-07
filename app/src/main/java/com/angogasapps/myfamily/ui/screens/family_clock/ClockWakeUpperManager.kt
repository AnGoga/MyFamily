package com.angogasapps.myfamily.ui.screens.family_clock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import androidx.core.content.ContextCompat.getSystemService
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.models.family_clock.ClockObject
import java.util.*


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
        if (obj.time <= System.currentTimeMillis()) return

        val context: Context = AppApplication.getInstance()
        val intent = Intent(context, FamilyClockReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.RTC_WAKEUP, obj.time] = pendingIntent
    }

//    @Synchronized
//    fun wakeUpClock(obj: ClockObject, context: Context) {
//        var delta = (obj.time - System.currentTimeMillis()) / 1000
//        if (delta <= 0) return
//
//        val days = (delta - (delta % 86_400)) / 86_400
//        delta -= days * 86_400
//        val hours = (delta - (delta % 3600)) / 3600
//        delta -= hours * 3600
//        val minutes = (delta - (delta % 60)) / 60
//
//        val intent = Intent(AlarmClock.ACTION_SET_ALARM)
//        intent.putExtra(AlarmClock.EXTRA_DAYS, days)
//        intent.putExtra(AlarmClock.EXTRA_HOUR, hours)
//        intent.putExtra(AlarmClock.EXTRA_MINUTES, minutes)
//        intent.putExtra(AlarmClock.EXTRA_SKIP_UI, false)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        context.startActivity(intent)
//    }
}
