package com.angogasapps.myfamily.async.notification

import android.util.Log

import com.angogasapps.myfamily.app.AppApplication.Companion.getInstance
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts
import org.json.JSONObject
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.models.Message
import java.lang.Exception
import java.lang.StringBuilder


fun fromChatMessage(message: Message, to: String = FirebaseVarsAndConsts.USER.family + "-chat"): JSONObject {
    var notifyObject = JSONObject()
    try {
        notifyObject = buildFrom(message, to)
    } catch (e: Exception) {
        Log.e("tag", "ERROR on build FcmMessage")
    }
    return notifyObject
}

private fun buildFrom(message: Message, to: String): JSONObject {
    val builder = FcmMessageBuilder()
    builder.setTo(to)
    builder.setTitle(FirebaseVarsAndConsts.USER.name)
    builder.setBody(getTextToChatMessageNotification(message))
    if (message.type == FirebaseVarsAndConsts.TYPE_IMAGE_MESSAGE) {
        builder.setImage(message.value!!)
    }
    return builder.build()
}

private fun getTextToChatMessageNotification(message: Message): String {
    var context = getInstance().applicationContext
    val string = StringBuilder()
    when (message.type) {
        FirebaseVarsAndConsts.TYPE_TEXT_MESSAGE -> string.append(message.value)
        FirebaseVarsAndConsts.TYPE_IMAGE_MESSAGE -> string.append("\uD83D\uDCF7" + " ").append(context.getString(R.string.photo))
        FirebaseVarsAndConsts.TYPE_VOICE_MESSAGE -> string.append("\uD83C\uDFA4" + " ").append(context!!.getString(R.string.voice))
    }
    context = null
    return string.toString()
}
