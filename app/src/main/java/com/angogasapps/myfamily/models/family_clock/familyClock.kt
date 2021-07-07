package com.angogasapps.myfamily.models.family_clock

import com.angogasapps.myfamily.firebase.EFirebaseEvents
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*

data class ClockObject(
        var id: String,
        var to: ArrayList<String>, // users id
        var fromPhone: String,
        var fromId: String,
        var time: Long, //in millis
        var options: HashMap<String, String>?= null

){
    fun buildHashMap(): HashMap<String, Any>{
        val map = HashMap<String, Any>()
        map[CHILD_ID] = id
        map[CHILD_TO] = to
        map[CHILD_FROM_PHONE] = fromPhone
        map[CHILD_FROM_ID] = fromId
        map[CHILD_TIME] = time
        options?.let {
            map[CHILD_OPTIONS] = it
        }
        return map
    }
}

data class FamilyClockEvent(
        val event: EFirebaseEvents,
        val value: ClockObject
)