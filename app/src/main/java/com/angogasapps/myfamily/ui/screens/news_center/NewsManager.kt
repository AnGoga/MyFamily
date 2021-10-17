package com.angogasapps.myfamily.ui.screens.news_center

import com.angogasapps.myfamily.models.events.NewsObject.Companion.from
import com.angogasapps.myfamily.models.events.NewsObject.Companion.isCanLife
import com.angogasapps.myfamily.utils.getIndexOfDeleteNews
import io.reactivex.subjects.PublishSubject
import com.angogasapps.myfamily.models.events.NewsEvent
import com.google.firebase.database.ChildEventListener
import com.angogasapps.myfamily.models.events.NewsObject
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts
import com.angogasapps.myfamily.firebase.NewsCenterFunks
import java.util.ArrayList

object NewsManager {
    val subject = PublishSubject.create<NewsEvent>()
    private lateinit var listener: ChildEventListener

    val allNews = ArrayList<NewsObject>()



    init {
        initManager()
    }


    private fun initManager() {
        listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                this@NewsManager.onChildAdded(snapshot)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {
                this@NewsManager.onChildRemoved(snapshot)
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        }
        FirebaseVarsAndConsts.DATABASE_ROOT.child(FirebaseVarsAndConsts.NODE_NEWS)
            .child(FirebaseVarsAndConsts.USER.family).addChildEventListener(listener)
    }

    @Synchronized
    private fun onChildAdded(snapshot: DataSnapshot) {
        val obj = from(snapshot)
        if (isCanLife(obj)) {
            allNews.add(obj)
            val event = NewsEvent()
            event.event = NewsEvent.ENewsEvents.added
            event.newsId = obj.id
            event.index = allNews.size - 1
            subject.onNext(event)
        } else {
            NewsCenterFunks.deleteNewsObject(obj)
        }
    }

    @Synchronized
    private fun onChildRemoved(snapshot: DataSnapshot) {
        val obj = from(snapshot)
        val index = getIndexOfDeleteNews(allNews, obj)
        if (index == -1) return
        allNews.removeAt(index)
        val event = NewsEvent()
        event.index = index
        event.newsId = obj.id
        event.event = NewsEvent.ENewsEvents.removed
        subject.onNext(event)
    }
}