package com.angogasapps.myfamily.ui.screens.family_clock

import com.angogasapps.myfamily.firebase.EFirebaseEvents
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*
import com.angogasapps.myfamily.models.Family
import com.angogasapps.myfamily.models.family_clock.ClockObject
import com.angogasapps.myfamily.models.family_clock.FamilyClockEvent
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import javax.inject.Singleton


class FamilyClockManager private constructor(scope: CoroutineScope){
    val path = DATABASE_ROOT.child(NODE_CLOCK).child(USER.family).child(USER.id)
    val channel: BroadcastChannel<FamilyClockEvent> = BroadcastChannel(1)

    companion object {
        private var manager: FamilyClockManager? = null

        fun getInstance(scope: CoroutineScope): FamilyClockManager{
            if (FamilyClockManager == null)
                manager = FamilyClockManager(scope)
            return manager!!
        }
    }

    init {
        initListener()
    }

    private fun initListener() {
        path.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val value = snapshot.getValue(ClockObject::class.java)!!
                val event = FamilyClockEvent(EFirebaseEvents.added, value)
                channel.trySend(event)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val value = snapshot.getValue(ClockObject::class.java)!!
                val event = FamilyClockEvent(EFirebaseEvents.changed, value)
                channel.trySend(event)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val value = snapshot.getValue(ClockObject::class.java)!!
                val event = FamilyClockEvent(EFirebaseEvents.removed, value)
                channel.trySend(event)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

        })
    }
}