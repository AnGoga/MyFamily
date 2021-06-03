package com.angogasapps.myfamily.app

import android.app.ActivityManager
import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.angogasapps.myfamily.ui.screens.personal_dairy.DairyDatabaseManager
import java.lang.Thread.MAX_PRIORITY


class OnAppDeleteMaker {
    var exit = false
    var am: ActivityManager? = null
    var context: Context? = null

    fun deleteInternalStorage(context: Context?){
        this.context = context
        am = context!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        Looper.prepare();
        while(!exit){
            val taskInfo: List<ActivityManager.RunningTaskInfo> = am!!.getRunningTasks(MAX_PRIORITY)
            val activityName: String = taskInfo[0].topActivity!!.className
            Log.d("topActivity", "CURRENT Activity ::$activityName")
            if (activityName == "com.android.packageinstaller.UninstallerActivity") {
            //пользователь нажал кнопку удаления
                DairyDatabaseManager.getInstance().onDeleteApp()
                exit = true
            } else if(activityName == "com.android.settings.ManageApplications") {
                exit = true;
            }
        }
        Looper.loop();
    }
}