package com.angogasapps.myfamily.firebase

import android.net.Uri
import com.angogasapps.myfamily.firebase.DATABASE_ROOT
import com.angogasapps.myfamily.firebase.NODE_NEWS
import com.angogasapps.myfamily.firebase.STORAGE_ROOT
import com.angogasapps.myfamily.firebase.USER
import com.angogasapps.myfamily.utils.getMap
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import com.angogasapps.myfamily.models.events.NewsObject
import com.google.firebase.database.DatabaseReference
import com.angogasapps.myfamily.firebase.NewsCenterFunks
import com.google.firebase.storage.StorageReference
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask

object NewsCenterFunks {
    @Synchronized
    fun createNewTextNews(newsText: String, i: IOnEndCommunicationWithFirebase) {
        val newsObject = NewsObject()
        newsObject.type = NewsObject.TYPE_TEXT
        newsObject.value = newsText
        newsObject.fromPhone = USER.phone
        val ref: DatabaseReference = DATABASE_ROOT.child(NODE_NEWS).child(USER.family)
        val key = ref.push().key!!
        sendNewNews(newsObject, key, i)
    }

    @Synchronized
    fun createNewImageNews(uri: Uri?, i: IOnEndCommunicationWithFirebase) {
        val ref: DatabaseReference = DATABASE_ROOT.child(NODE_NEWS).child(USER.family)
        val key = ref.push().key!!
        val path: StorageReference = STORAGE_ROOT.child(NODE_NEWS).child(USER.family).child(key)
        path.putFile(uri!!).addOnCompleteListener { task1: Task<UploadTask.TaskSnapshot?> ->
            if (task1.isSuccessful) {
                path.downloadUrl.addOnCompleteListener { task2: Task<Uri> ->
                    if (task2.isSuccessful) {
                        val url = task2.result.toString()
                        val newsObject = NewsObject()
                        newsObject.value = url
                        newsObject.fromPhone = USER.phone
                        newsObject.type = NewsObject.TYPE_IMAGE
                        sendNewNews(newsObject, key, i)
                    } else {
                        task2.exception.toString()
                        i.onFailure()
                    }
                }
            } else {
                task1.exception!!.printStackTrace()
                i.onFailure()
            }
        }
    }

    @Synchronized
    private fun sendNewNews(
        newsObject: NewsObject,
        key: String,
        i: IOnEndCommunicationWithFirebase
    ) {
        DATABASE_ROOT.child(NODE_NEWS).child(USER.family).child(key)
            .updateChildren(getMap(newsObject))
            .addOnCompleteListener(OnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    i.onSuccess()
                } else {
                    task.exception!!.printStackTrace()
                    i.onFailure()
                }
            })
    }

    @Synchronized
    fun deleteNewsObject(`object`: NewsObject) {
        DATABASE_ROOT.child(NODE_NEWS).child(USER.family).child(`object`.id).removeValue()
    }
}