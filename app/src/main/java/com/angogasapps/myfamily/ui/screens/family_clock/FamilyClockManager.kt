package com.angogasapps.myfamily.ui.screens.family_clock

import com.angogasapps.myfamily.firebase.EFirebaseEvents
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.models.Family
import com.angogasapps.myfamily.models.family_clock.ClockObject
import com.angogasapps.myfamily.models.family_clock.FamilyClockEvent
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel


class FamilyClockManager private constructor(val scope: CoroutineScope){
    val path = DATABASE_ROOT.child(NODE_CLOCK).child(USER.family).child(USER.id)
    val channel: BroadcastChannel<FamilyClockEvent> = BroadcastChannel(1)
    lateinit var listener: ChildEventListener

    companion object {
        private var manager: FamilyClockManager? = null

        fun getInstance(scope: CoroutineScope): FamilyClockManager {
            if (manager == null)
                manager = FamilyClockManager(scope)
            return manager!!
        }

        fun killManager() {
            manager?.let {
                it.path.removeEventListener(it.listener)
                it.channel.cancel()
                manager = null
            }
        }
    }

    init {
        initListener()
    }

    private fun initListener() {
        listener = object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                onGetEvent(snapshot, EFirebaseEvents.added)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                onGetEvent(snapshot, EFirebaseEvents.changed)
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                onGetEvent(snapshot, EFirebaseEvents.removed)
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        }
        path.addChildEventListener(listener)
    }

    private fun onGetEvent(snapshot: DataSnapshot, event: EFirebaseEvents){
        channel.trySend(FamilyClockEvent(event, snapshot.getValue(ClockObject::class.java)!!))
    }
}