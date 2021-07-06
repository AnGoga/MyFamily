package com.angogasapps.myfamily.models.family_clock

import com.angogasapps.myfamily.firebase.EFirebaseEvents

data class ClockObject(
        val id: String,
        var fromPhone: String,
        var fromId: String,
        var time: Long,
        var melodyValue: String? = null
)

data class FamilyClockEvent(
        val event: EFirebaseEvents,
        val value: ClockObject
)