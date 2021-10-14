package com.angogasapps.myfamily.utils

import com.angogasapps.myfamily.models.events.NewsObject
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts
import com.google.firebase.database.ServerValue
import java.util.ArrayList
import java.util.HashMap


fun getMap(obj: NewsObject): HashMap<String, Any> {
    val map = HashMap<String, Any>()
    map[FirebaseVarsAndConsts.CHILD_TYPE] = obj.type
    map[FirebaseVarsAndConsts.CHILD_FROM_PHONE] = obj.fromPhone
    map[FirebaseVarsAndConsts.CHILD_VALUE] = obj.value
    map[FirebaseVarsAndConsts.CHILD_TIME_CREATE] = ServerValue.TIMESTAMP
    return map
}

fun getIndexOfDeleteNews(newsList: ArrayList<NewsObject>, newNews: NewsObject): Int {
    for (i in newsList.indices) {
        if (newsList[i].id == newNews.id) {
            return i
        }
    }
    return -1
}
