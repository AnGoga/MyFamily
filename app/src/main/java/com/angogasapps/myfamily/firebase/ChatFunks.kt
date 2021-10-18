package com.angogasapps.myfamily.firebase

import com.angogasapps.myfamily.app.AppApplication.Companion.isOnline
import com.angogasapps.myfamily.app.AppApplication.Companion.getInstance
import com.angogasapps.myfamily.async.notification.FcmMessageManager.sendChatNotificationMessage
import com.angogasapps.myfamily.utils.Async
import com.angogasapps.myfamily.app.AppApplication
import android.widget.Toast
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.firebase.ChatFunks
import com.angogasapps.myfamily.firebase.FirebaseHelper
import com.google.firebase.database.DatabaseReference
import com.angogasapps.myfamily.firebase.*
import com.google.firebase.database.ServerValue
import com.google.android.gms.tasks.OnCompleteListener
import com.angogasapps.myfamily.async.notification.FcmMessageManager
import com.google.firebase.storage.StorageReference
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableEmitter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import com.angogasapps.myfamily.models.Message
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.UploadTask
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.io.File
import java.net.URL
import java.util.HashMap

object ChatFunks {
    fun sendMessage(type: String, value: String) {
        Async.runInNewThread {
            if (!isOnline) Toast.makeText(
                getInstance().applicationContext,
                R.string.have_not_internet_connection,
                Toast.LENGTH_SHORT
            ).show()
            sendMessageWithKey(type, value, FirebaseHelper.messageKey!!)
        }
    }

    fun sendMessageWithKey(type: String, value: String, key: String) {
        var value = value
        val path = DATABASE_ROOT.child(NODE_CHAT)
            .child(USER.family)
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
                sendChatNotificationMessage(message)
            }
        }
    }

    fun sendImage(imageUri: Uri) {
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

    fun sendVoice(voiceFile: File, key: String?) {
        val path =
            STORAGE_ROOT.child(FOLDER_VOICE_MESSAGE)
                .child(USER.family).child(
                key!!
            )
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

    fun downloadImageMessageAndSetBitmap(path: String, key: String, imageView: ImageView) {
        if (chatImageMessangesMap.containsKey(key)) {
            imageView.setImageBitmap(chatImageMessangesMap[key])
        } else {
            Observable.create { emitter: ObservableEmitter<Any?> ->
                val photoUrl = URL(path)
                val downloadStream = photoUrl.openStream()
                val bitmap = BitmapFactory.decodeStream(downloadStream)
                chatImageMessangesMap[key] = bitmap
                emitter.onNext(bitmap)
            }
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Any?> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(o: Any) {
                        imageView.setImageBitmap(chatImageMessangesMap[key])
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onComplete() {}
                })
        }
    }

    fun getFileFromStorage(file: File?, url: String?, i: IOnEndCommunicationWithFirebase) {
        STORAGE_ROOT.storage.getReference(url!!).getFile(file!!)
            .addOnCompleteListener { task: Task<FileDownloadTask.TaskSnapshot?> ->
                if (task.isSuccessful) {
                    i.onSuccess()
                } else {
                    i.onFailure()
                }
            }
    }

    fun getVoiceFileFromStorage(file: File, key: String, i: IOnEndCommunicationWithFirebase) {
        STORAGE_ROOT.child(FOLDER_VOICE_MESSAGE)
            .child(USER.family).child(
            key
        ).getFile(file)
            .addOnCompleteListener { task: Task<FileDownloadTask.TaskSnapshot?> -> if (task.isSuccessful) i.onSuccess() else i.onFailure() }
    }
}