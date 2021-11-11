package com.angogasapps.myfamily.network.firebaseImpl.news_center

import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.firebase.DATABASE_ROOT
import com.angogasapps.myfamily.firebase.NODE_NEWS
import com.angogasapps.myfamily.firebase.USER
import com.angogasapps.myfamily.models.events.NewsEvent
import com.angogasapps.myfamily.models.events.NewsObject
import com.angogasapps.myfamily.network.interfaces.news_center.NewsCenterListener
import com.angogasapps.myfamily.network.interfaces.news_center.NewsCenterService

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseNewsCenterListenerImpl @Inject constructor() : NewsCenterListener() {
    @Inject
    lateinit var newsService: NewsCenterService

    private val fbWebSocket = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            this@FirebaseNewsCenterListenerImpl.onChildAdded(snapshot)
        }
        override fun onChildRemoved(snapshot: DataSnapshot) {
            this@FirebaseNewsCenterListenerImpl.onChildRemoved(snapshot)
        }
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}
    }

    init {
        appComponent.inject(this)
        DATABASE_ROOT.child(NODE_NEWS)
            .child(USER.family).addChildEventListener(fbWebSocket)
    }

    private fun onChildAdded(snapshot: DataSnapshot) {
        val obj = NewsObject.from(snapshot)
        if (NewsObject.isCanLife(obj)) {
            val event = NewsEvent()
            event.event = NewsEvent.ENewsEvents.added
            event.value = obj
            scope.launch {
                flow.emit(event)
            }
        } else {
            newsService.deleteNewsObject(obj)
        }
    }

    private fun onChildRemoved(snapshot: DataSnapshot) {
        val obj = NewsObject.from(snapshot)
        val event = NewsEvent()
        event.value = obj
        event.event = NewsEvent.ENewsEvents.removed
        scope.launch {
            flow.emit(event)
        }
    }
}