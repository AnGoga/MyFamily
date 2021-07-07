package com.angogasapps.myfamily.firebase

import com.angogasapps.myfamily.async.notification.FcmMessageBuilder
import com.angogasapps.myfamily.async.notification.FcmMessageBuilder.CHILD_MESSAGE
import com.angogasapps.myfamily.async.notification.FcmMessageBuilder.CHILD_TOPIC
import com.angogasapps.myfamily.async.notification.FcmMessageManager
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*
import com.angogasapps.myfamily.models.Family
import com.angogasapps.myfamily.models.family_clock.ClockObject
import kotlinx.coroutines.*
import org.json.JSONObject
import java.lang.Exception
import kotlin.coroutines.coroutineContext

fun sendFamilyClock(obj: ClockObject, scope: CoroutineScope){
    val path = DATABASE_ROOT.child(NODE_CLOCK).child(USER.family)
    if (obj.id == ""){
        obj.id = path.push().key!!
    }
    path.child(obj.id).updateChildren(obj.buildHashMap()).addOnCompleteListener {
        if (it.isSuccessful){
            sendFCM(obj, scope)
        }else {
            it.exception?.printStackTrace()
        }
    }
}

private fun sendFCM(obj: ClockObject, scope: CoroutineScope) = scope.launch(Dispatchers.Default) {
    for (to in obj.to)
        try {
            FcmMessageManager.sendNotificationMessage(buildJSON(obj, to))
        }catch (e: Exception){ e.printStackTrace() }

}

private fun buildJSON(obj: ClockObject, to: String): JSONObject {
    try {
        val json = JSONObject()
        val data = JSONObject()
        obj.options?.let { for (key in it.keys) data.put(key, it[key]) }

        data.put(CHILD_TIME, obj.time.toString())
        data.put(CHILD_FROM_ID, obj.fromId)
        data.put(CHILD_FROM_PHONE, obj.fromPhone)
        data.put(CHILD_ID, obj.id)
        data.put(CHILD_TYPE, TYPE_ALARM_CLOCK_MESSAGE)

        json.put(CHILD_TOPIC, to)
        json.put(CHILD_DATA, data)

        return JSONObject().put(CHILD_MESSAGE, json)
    }catch (e: Exception){
        e.printStackTrace()
    }
    return JSONObject()
}

