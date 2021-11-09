package com.angogasapps.myfamily.network.firebaseImpl.chat

import com.angogasapps.myfamily.firebase.DATABASE_ROOT
import com.angogasapps.myfamily.firebase.NODE_CHAT
import com.angogasapps.myfamily.firebase.USER
import com.angogasapps.myfamily.models.Message
import com.angogasapps.myfamily.network.interfaces.chat.ChatListener
import com.angogasapps.myfamily.ui.screens.chat.ChatEvent
import com.angogasapps.myfamily.ui.screens.chat.EChatEvent
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.launch
import javax.inject.Inject

class FirebaseChatListenerImpl @Inject constructor() : ChatListener() {
    private var isHaveFirstDownload = false
    private val chatRef = DATABASE_ROOT.child(NODE_CHAT).child(USER.family)
    private val firebaseListener = object: ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val message = Message(snapshot)
            val event = ChatEvent(EChatEvent.added, message)
            if (!isHaveFirstDownload) {
                isHaveFirstDownload = !isHaveFirstDownload;
            } else {
                scope.launch {
                    flow.emit(event)
                }
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}

    }


    init {
        initFirebaseListener()
    }

    private fun initFirebaseListener() {
        chatRef.limitToLast(1).addChildEventListener(firebaseListener)
    }

}