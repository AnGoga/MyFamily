package com.angogasapps.myfamily.app

import android.app.Application
import com.angogasapps.myfamily.database.DatabaseManager
import es.dmoral.toasty.Toasty
import com.angogasapps.myfamily.R
import androidx.annotation.StringRes
import java.lang.Exception

class AppApplication : Application() {

    companion object {
        lateinit var app: AppApplication
            private set


        @JvmStatic
        val isOnline: Boolean
            get() {
                val runtime = Runtime.getRuntime()
                try {
                    val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
                    val exitValue = ipProcess.waitFor()
                    return exitValue == 0
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return false
            }

        @JvmStatic
        fun getInstance() = app
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        DatabaseManager.getInstance()
        AppNotificationManager.createNotificationChanel(applicationContext)
    }
}