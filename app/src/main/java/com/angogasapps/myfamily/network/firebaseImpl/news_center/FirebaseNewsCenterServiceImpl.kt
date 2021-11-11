package com.angogasapps.myfamily.network.firebaseImpl.news_center

import android.net.Uri
import com.angogasapps.myfamily.firebase.DATABASE_ROOT
import com.angogasapps.myfamily.firebase.NODE_NEWS
import com.angogasapps.myfamily.firebase.STORAGE_ROOT
import com.angogasapps.myfamily.firebase.USER
import com.angogasapps.myfamily.models.events.NewsObject
import com.angogasapps.myfamily.network.interfaces.news_center.NewsCenterService
import com.angogasapps.myfamily.utils.getMap
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseNewsCenterServiceImpl @Inject constructor() : NewsCenterService{

    override fun createNewTextNews(newsText: String, onSuccess: () -> Unit, onError: () -> Unit) {
        val newsObject = NewsObject()
        newsObject.type = NewsObject.TYPE_TEXT
        newsObject.value = newsText
        newsObject.fromPhone = USER.phone
        val ref: DatabaseReference = DATABASE_ROOT.child(NODE_NEWS).child(USER.family)
        val key = ref.push().key!!
        sendNewNews(newsObject, key, onSuccess, onError)
    }

    override fun createNewImageNews(uri: Uri?, onSuccess: () -> Unit, onError: () -> Unit) {
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
                        sendNewNews(newsObject, key, onSuccess, onError)
                    } else {
                        task2.exception?.printStackTrace()
                        onError()
                    }
                }
            } else {
                task1.exception!!.printStackTrace()
                onError()
            }
        }
    }

    private fun sendNewNews(
        newsObject: NewsObject,
        key: String,
        onSuccess: () -> Unit, onError: () -> Unit
    ) {
        DATABASE_ROOT.child(NODE_NEWS).child(USER.family).child(key)
            .updateChildren(getMap(newsObject))
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    task.exception?.printStackTrace()
                    onError()
                }
            }
    }

    override fun deleteNewsObject(obj: NewsObject) {
        DATABASE_ROOT.child(NODE_NEWS).child(USER.family).child(obj.id).removeValue()
    }
}