package com.angogasapps.myfamily.network.interfaces.news_center

import android.net.Uri
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import com.angogasapps.myfamily.models.events.NewsObject

interface NewsCenterService {
    fun createNewTextNews(newsText: String, onSuccess: () -> Unit, onError: () -> Unit)
    fun createNewImageNews(uri: Uri?, onSuccess: () -> Unit, onError: () -> Unit)
    fun deleteNewsObject(obj: NewsObject)
}