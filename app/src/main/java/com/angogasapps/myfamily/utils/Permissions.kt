package com.angogasapps.myfamily.utils

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

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
}