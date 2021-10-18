package com.angogasapps.myfamily.utils

import com.angogasapps.myfamily.models.events.NewsObject
import com.angogasapps.myfamily.firebase.*
import com.google.firebase.database.ServerValue
import java.util.ArrayList
import java.util.HashMap


fun getMap(obj: NewsObject): HashMap<String, Any> {
    val map = HashMap<String, Any>()
    map[CHILD_TYPE] = obj.type
    map[CHILD_FROM_PHONE] = obj.fromPhone
    map[CHILD_VALUE] = obj.value
    map[CHILD_TIME_CREATE] = ServerValue.TIMESTAMP
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
