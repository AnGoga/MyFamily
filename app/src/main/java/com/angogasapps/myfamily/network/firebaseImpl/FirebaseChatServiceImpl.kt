package com.angogasapps.myfamily.network.firebaseImpl

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.async.notification.FcmMessageManager
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import com.angogasapps.myfamily.models.Message
import com.angogasapps.myfamily.network.Result
import com.angogasapps.myfamily.network.interfaces.ChatService
import com.angogasapps.myfamily.objects.ChatChildEventListener
import com.angogasapps.myfamily.ui.screens.chat.ChatEvent
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.*

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.tasks.await
import java.io.File
import java.net.URL
import java.util.HashMap
import javax.inject.Inject

class FirebaseChatServiceImpl @Inject constructor() : ChatService {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val chatRef = DATABASE_ROOT.child(NODE_CHAT).child(USER.family)

    private var totalMessageCount = 0;

    private val listener: ValueEventListener = object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {}
        override fun onCancelled(error: DatabaseError) {}
    }
//    private val listener: ChatChildEventListener = ChatChildEventListener(::onGetMessageFromFirebase)




    override suspend fun getMoreMessages(fromMessage: Message, count: Int): Result<List<Message>> {
        totalMessageCount += count
        try {
            val res = chatRef.limitToLast(totalMessageCount - 1).get().await()
            val list = ArrayList<Message>()
            for (message in res.children) {
                list.add(Message(message))
            }
            list.sort()
//            val ind = if (fromMessage.id == "") count else list.indexOf(fromMessage)
            return Result.Success(list.subList(0, count - 1)/*.also { if (it.isNotEmpty()) it.removeAt(it.size - 1) }*/)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(e)
        }
    }






    override fun sendMessage(type: String, value: String) {
        scope.launch {
            sendMessageWithKey(type, value, FirebaseHelper.messageKey!!)
            if (!AppApplication.isOnline) Toast.makeText(
                AppApplication.getInstance().applicationContext,
                R.string.have_not_internet_connection,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun sendMessageWithKey(type: String, value: String, key: String) {
        var value = value
        val path = DATABASE_ROOT.child(NODE_CHAT).child(USER.family)
        if (type == TYPE_TEXT_MESSAGE) {
            value = value.trim { it <= ' ' }
        }
        val messageMap = HashMap<String, Any>()
        messageMap[CHILD_FROM] = UID
        messageMap[CHILD_TYPE] = type
        messageMap[CHILD_VALUE] = value
        messageMap[CHILD_TIME] = ServerValue.TIMESTAMP
        val finalValue = value

        path.child(key).updateChildren(messageMap).addOnCompleteListener { task: Task<Void?> ->
            if (task.isSuccessful) {
                val message = Message()
                message.from = UID
                message.type = type
                message.value = finalValue
                FcmMessageManager.sendChatNotificationMessage(message)
            }
        }
    }

    override fun sendImage(imageUri: Uri) {
        val key = FirebaseHelper.messageKey
        val path =
            STORAGE_ROOT.child(FOLDER_IMAGE_MESSAGE)
                .child(USER.family).child(key)
        path.putFile(imageUri).addOnCompleteListener { task1: Task<UploadTask.TaskSnapshot?> ->
            if (task1.isSuccessful) {
                path.downloadUrl.addOnCompleteListener { task2: Task<Uri> ->
                    if (task2.isSuccessful) {
                        val url = task2.result.toString()
                        sendMessageWithKey(TYPE_IMAGE_MESSAGE, url, key)
                    }
                }
            }
        }
    }

    override fun sendVoice(voiceFile: File, key: String?) {
        val path = STORAGE_ROOT.child(FOLDER_VOICE_MESSAGE)
            .child(USER.family).child(key!!)
        path.putFile(Uri.fromFile(voiceFile))
            .addOnCompleteListener { task1: Task<UploadTask.TaskSnapshot?> ->
                if (task1.isSuccessful) {
                    path.downloadUrl.addOnCompleteListener { task2: Task<Uri> ->
                        if (task2.isSuccessful) {
                            val url = task2.result.toString()
                            sendMessageWithKey(TYPE_VOICE_MESSAGE, url, key)
                        }
                    }
                }
            }
    }


    override fun downloadImageMessageAndSetBitmap(path: String, key: String, imageView: ImageView) {
        scope.launch(Dispatchers.IO) {
            if (chatImageMessangesMap.containsKey(key)) {
                withContext(Dispatchers.Main) {
                    imageView.setImageBitmap(chatImageMessangesMap[key])
                }
            } else {
                val photoUrl = URL(path)
                val downloadStream = photoUrl.openStream()
                val bitmap = BitmapFactory.decodeStream(downloadStream)
                chatImageMessangesMap[key] = bitmap
                withContext(Dispatchers.Main) {
                    imageView.setImageBitmap(bitmap)
                }
            }
        }
    }

    override fun getVoiceFileFromStorage(
        file: File,
        key: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        STORAGE_ROOT.child(FOLDER_VOICE_MESSAGE).child(USER.family).child(key).getFile(file)
            .addOnCompleteListener { task: Task<FileDownloadTask.TaskSnapshot?> ->
                if (task.isSuccessful) onSuccess()
                else onFailure()
            }
    }
}