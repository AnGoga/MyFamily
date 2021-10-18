package com.angogasapps.myfamily.async.notification

import android.content.Context
import com.angogasapps.myfamily.async.notification.fromChatMessage
import com.angogasapps.myfamily.app.AppApplication.Companion.getInstance
import com.angogasapps.myfamily.utils.Async
import org.json.JSONObject
import kotlin.Throws
import android.content.res.AssetManager
import com.angogasapps.myfamily.app.AppApplication
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.firebase.messaging.FirebaseMessaging
import com.angogasapps.myfamily.firebase.*
import com.google.android.gms.tasks.OnCompleteListener
import android.content.SharedPreferences
import android.util.Log
import com.angogasapps.myfamily.models.Message
import com.google.android.gms.tasks.Task
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


private const val postUrl = "https://fcm.googleapis.com/v1/projects/myfamily-1601b/messages:send"
private const val MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging"
private val SCOPES = arrayOf(MESSAGING_SCOPE)

object FcmMessageManager {
    fun sendChatNotificationMessage(message: Message) {
        Async.runInNewThread {
            try {
                val messageObject = fromChatMessage(message)
                Log.d("TAG", "sendMessage: \n$messageObject")
                sendNotificationMessage(messageObject)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @Throws(IOException::class)
    fun sendNotificationMessage(fcmMessage: JSONObject) {
        val connection = connection
        connection.doOutput = true
        val outputStream = DataOutputStream(connection.outputStream)
        outputStream.writeBytes(fcmMessage.toString())
        outputStream.flush()
        outputStream.close()
        val responseCode = connection.responseCode
        if (responseCode == 200) {
            val response = inputstreamToString(connection.inputStream)
            println("Message успешно отправлено в Firebase")
            println(response)
        } else {
            println("При отправке message в Firebase произошла ошибка")
            val response = inputstreamToString(connection.errorStream)
            println(response)
        }
    }

    @get:Throws(IOException::class)
    private val connection: HttpURLConnection
        private get() {
            val url = URL(postUrl)
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + accessToken)
            httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8")
            return httpURLConnection
        }

    @get:Throws(IOException::class)
    private val accessToken: String
        private get() {
            val assetManager = getInstance().assets
            val googleCredential = GoogleCredential
                .fromStream(assetManager.open("service-account.json"))
                .createScoped(Arrays.asList(*SCOPES))
            googleCredential.refreshToken()
            return googleCredential.accessToken
        }

    @Throws(IOException::class)
    private fun inputstreamToString(inputStream: InputStream): String {
        val stringBuilder = StringBuilder()
        val scanner = Scanner(inputStream)
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine())
        }
        return stringBuilder.toString()
    }

    fun subscribeToTopics() {
        if (canSubscribeToFamily()) {
            FirebaseMessaging.getInstance()
                .subscribeToTopic(USER.family + "-chat")
                .addOnCompleteListener { task: Task<Void?> ->
                    Log.d(
                        "TAG",
                        "subscribeToFamily: $task"
                    )
                }
        }
        FirebaseMessaging.getInstance().subscribeToTopic(USER.id)
            .addOnCompleteListener { task: Task<Void?> -> Log.d("TAG", "subscribeToSelf: $task") }
    }

    fun unsubscribeFromFamilyChat() {
        FirebaseMessaging.getInstance()
            .unsubscribeFromTopic(USER.family + "-chat")
            .addOnCompleteListener { task: Task<Void?> ->
                Log.d(
                    "TAG",
                    "unsubscribeFromFamily: $task"
                )
            }
    }

    private fun canSubscribeToFamily(): Boolean {
        val sf = getInstance().getSharedPreferences("family-chat", Context.MODE_PRIVATE)
        return sf.getBoolean("can_send_notification", true)
    }

    fun setPermissionToGetChatNotifications(isHavePermission: Boolean) {
        val sf = getInstance().getSharedPreferences("family-chat", Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putBoolean("can_send_notification", isHavePermission)
        editor.apply()
        if (isHavePermission) {
            subscribeToTopics()
        } else {
            unsubscribeFromFamilyChat()
        }
    }

    fun updateToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String> ->
            if (task.isSuccessful) {
                USER.token = task.result
                DATABASE_ROOT.child(NODE_USERS).child(
                    AUTH.currentUser!!.uid
                ).child(CHILD_TOKEN)
                    .setValue(task.result.toString()).addOnCompleteListener { task1: Task<Void?> ->
                        if (task1.isSuccessful) {
                        } else {
                            task1.exception!!.printStackTrace()
                        }
                    }
            }
        }
    }
}
