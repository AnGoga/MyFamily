package com.angogasapps.myfamily.utils

import com.angogasapps.myfamily.app.AppApplication.Companion.getInstance
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.R
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

object StringFormater {
    @JvmStatic
    fun formatLongToTime(time: Long?): String {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.format(Date(time!!))
    }

    @JvmStatic
    fun formatToRole(role: String?): String {
        if (role == null) return ""
        if (role == FirebaseVarsAndConsts.ROLE_MEMBER) return getInstance().getString(R.string.participant)
        return if (role == FirebaseVarsAndConsts.ROLE_CREATOR) getInstance().getString(R.string.creator) else role
    }

    fun convertUTF8ToString(s: String): String {
        return String(s.toByteArray(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)
    }

    @JvmStatic
    fun convertStringToUTF8(s: String): String {
        return String(s.toByteArray(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)
    }
}