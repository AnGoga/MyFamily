package com.angogasapps.myfamily.models.family_clock

import com.angogasapps.myfamily.firebase.EFirebaseEvents

data class ClockObject(
        val id: String,
        var to: ArrayList<String>, // users id
        var fromPhone: String,
        var fromId: String,
        var time: Long,
        var options: HashMap<String, String>?= null
)

data class FamilyClockEvent(
        val event: EFirebaseEvents,
        val value: ClockObject
)