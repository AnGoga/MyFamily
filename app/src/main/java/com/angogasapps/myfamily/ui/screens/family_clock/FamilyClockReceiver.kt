package com.angogasapps.myfamily.ui.screens.family_clock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.angogasapps.myfamily.ui.screens.splash.SplashActivity

class FamilyClockReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        println(intent.toString() + Thread.currentThread())

        val intent = Intent(context, AlarmClockActivity::class.java)
                .also { it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); }

        context.startActivity(intent)

    }
}