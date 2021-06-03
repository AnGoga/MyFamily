package com.angogasapps.myfamily.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class UninstallIntentReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val packageNames = intent!!.getStringArrayExtra("android.intent.extra.PACKAGES")

        if (packageNames != null) {
            for (packageName in packageNames) {
                if (packageName != null && packageName == context?.applicationContext?.packageName) {
                    OnAppDeleteMaker().deleteInternalStorage(context)
                }
            }
        }
    }
}