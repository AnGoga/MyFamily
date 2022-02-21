package com.angogasapps.myfamily.utils

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

import android.content.Intent
import android.os.Environment
import android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import com.angogasapps.myfamily.app.appComponent


object Permissions {
    const val AUDIO_RECORD_PERM = Manifest.permission.RECORD_AUDIO
    const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    const val PERMISSION_CALLBACK = 12345
    fun havePermission(permission: String, activity: Activity?): Boolean {
        return if (Build.VERSION.SDK_INT >= 23
          && ContextCompat.checkSelfPermission(activity!!, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), PERMISSION_CALLBACK)
              false
          } else {
              true
        }
    }

    fun hasStoragePermission(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent()
                intent.action = ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                activity.startActivity(intent)
            }
            return Environment.isExternalStorageManager()
        }
        return true
    }
}