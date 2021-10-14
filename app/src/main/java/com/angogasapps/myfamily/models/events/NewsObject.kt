package com.angogasapps.myfamily.models.events

import com.google.firebase.database.DataSnapshot

data class NewsObject(
    var fromPhone: String = "",
    var id: String = "",
    var type: String = "",
    var value: String = "",
    var timeCreate: Long = 0
) {


    companion object {
        const val TYPE_TEXT = "text"
        const val TYPE_IMAGE = "image"
        const val TYPE_VIDEO = "video"
        const val timeLive = (8 * 60 * 60 * 1000).toLong()

        @JvmStatic
        fun from(snapshot: DataSnapshot): NewsObject {
            val obj = snapshot.getValue(NewsObject::class.java)!!
            obj.id = snapshot.key!!
            return obj
        }

        @JvmStatic
        fun isCanLife(obj: NewsObject): Boolean {
            return obj.timeCreate + timeLive > System.currentTimeMillis()
        }
    }
}