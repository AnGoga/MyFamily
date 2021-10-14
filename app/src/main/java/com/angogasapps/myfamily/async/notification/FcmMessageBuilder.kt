package com.angogasapps.myfamily.async.notification

import android.util.Log
import com.angogasapps.myfamily.utils.StringFormater.convertStringToUTF8
import com.angogasapps.myfamily.async.notification.FcmMessageBuilder
import org.json.JSONObject
import com.angogasapps.myfamily.utils.StringFormater
import com.google.gson.JsonObject
import java.lang.Exception
import java.util.HashMap

class FcmMessageBuilder {
    private var to = ""
    private var title = ""
    private var body = ""
    private var image = ""
    private var data = HashMap<String, String>()
    fun setTo(to: String): FcmMessageBuilder {
        this.to = to
        return this
    }

    fun setTitle(title: String): FcmMessageBuilder {
        this.title = title
        return this
    }

    fun setBody(body: String): FcmMessageBuilder {
        this.body = body
        return this
    }

    fun setData(data: HashMap<String, String>): FcmMessageBuilder {
        this.data = data
        return this
    }

    fun setImage(imageUrl: String): FcmMessageBuilder {
        image = imageUrl
        return this
    }

    fun build(): JSONObject {
        return buildMessage()
    }

    private fun buildMessage(): JSONObject {
        return try {
            val mainObj = JSONObject()
            val messageObj = JSONObject()
            val notificationObj = JSONObject()
            val dataObj = JSONObject()
            notificationObj.put(
                CHILD_TITLE, convertStringToUTF8(
                    title
                )
            )
            notificationObj.put(
                CHILD_BODY, convertStringToUTF8(
                    body
                )
            )
            notificationObj.put(CHILD_IMAGE, image)
            for (key in data.keys) dataObj.put(key, data[key])
            messageObj.put(CHILD_TOPIC, to)
            messageObj.put(NODE_NOTIFICATION, notificationObj)
            messageObj.put(NODE_DATA, dataObj)
            mainObj.put(CHILD_MESSAGE, messageObj)
            mainObj
        } catch (e: Exception) {
            Log.e("tag", "ERROR on build FcmMessage")
            JSONObject()
        }
    }

    companion object {
        private const val NODE_DATA = "data"
        private const val NODE_NOTIFICATION = "notification"
        const val CHILD_TOPIC = "topic"
        private const val CHILD_TITLE = "title"
        private const val CHILD_BODY = "body"
        const val CHILD_MESSAGE = "message"
        private const val CHILD_IMAGE = "image"
    }
}