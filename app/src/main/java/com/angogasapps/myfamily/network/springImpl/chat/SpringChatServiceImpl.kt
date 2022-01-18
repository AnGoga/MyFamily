package com.angogasapps.myfamily.network.springImpl.chat

import android.net.Uri
import android.widget.ImageView
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.firebase.TYPE_IMAGE_MESSAGE
import com.angogasapps.myfamily.firebase.TYPE_VOICE_MESSAGE
import com.angogasapps.myfamily.firebase.USER
import com.angogasapps.myfamily.models.Message
import com.angogasapps.myfamily.network.Result
import com.angogasapps.myfamily.network.interfaces.chat.ChatService
import com.angogasapps.myfamily.network.retrofit.apiInterfaces.ChatAPI
import com.angogasapps.myfamily.network.retrofit.apiInterfaces.MediaStorageAPI
import com.angogasapps.myfamily.network.springImpl.media_storage.MediaStorageService
import com.angogasapps.myfamily.network.spring_models.chat.ChatPagingRequest
import com.angogasapps.myfamily.network.spring_models.chat.EMediaType
import com.angogasapps.myfamily.network.spring_models.chat.MediaFileInfo
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.Headers
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpringChatServiceImpl @Inject constructor(
    private val chatAPI: ChatAPI,
    private val storageAPI: MediaStorageAPI,
    private val storageService: MediaStorageService
) : ChatService {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override suspend fun getMoreMessages(fromMessage: Message, count: Int): Result<List<Message>> {
        return try {
            val list = chatAPI.getMoreMessages(
                familyId = USER.family,
                request = ChatPagingRequest(
                    familyId = USER.family,
                    count = count,
                    fromMessage = fromMessage.also { if (it.id == "") it.id = "-1" }
                )
            )
            Result.Success(data = list)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e = e)
        }
    }

    override fun sendMessage(type: String, value: String) {
        try {
            val job = scope.launch {
                chatAPI.sendMessage(
                    familyId = USER.family,
                    Message(type = type, value = value, from = USER.id)
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun sendImage(imageUri: Uri) {
        scope.launch {
            try {
                val info = MediaFileInfo(familyId = USER.family, type = EMediaType.CHAT_IMAGE)
                val file = File(imageUri.path)
                val loadedInfo = storageService.uploadFile(file, info)
                //TODO: отправить сообщение в Chat Service с какой-то ссылкой на эту картинку
                sendFileLoadedMessage(loadedInfo.fileInfo)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun sendVoice(voiceFile: File, key: String?) {
        scope.launch {
            try {
                val info = MediaFileInfo(familyId = USER.family, type = EMediaType.CHAT_VOICE)
                val loadedInfo = storageService.uploadFile(voiceFile, info)
                //TODO: отправить сообщение в Chat Service с какой-то ссылкой на этот файл
                sendFileLoadedMessage(loadedInfo.fileInfo)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //TODO: ?? отдельно для File TYPE и Image Type
    private suspend fun sendFileLoadedMessage(info: MediaFileInfo) {
        val message = Message().apply {
            from = USER.id
            value = info.id
            type =
                if (info.type == EMediaType.CHAT_VOICE) TYPE_VOICE_MESSAGE else TYPE_IMAGE_MESSAGE
        }
        chatAPI.sendMessage(familyId = USER.family, message)
    }

    override fun getVoiceFileFromStorage(
        file: File,
        key: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        scope.launch {
            try {
                val info = MediaFileInfo(familyId = USER.family, type = EMediaType.CHAT_VOICE)
                storageService.getFileFromServer(info, file)
                onSuccess()
            } catch (e: Exception) {
                onFailure()
                e.printStackTrace()
            }
        }
    }

    override fun downloadImageMessageAndSetBitmap(path: String, key: String, imageView: ImageView) {
        scope.launch {
            try {
//                val info = MediaFileInfo(id = key, familyId = USER.family, type = EMediaType.CHAT_IMAGE)
                val info =
                    MediaFileInfo(id = path, familyId = USER.family, type = EMediaType.CHAT_IMAGE)

                downloadImageAndSetBitmap(info, imageView)

//                val bitmap = storageService.getImageFromServer(info)
//                withContext(Dispatchers.Main) {
//                    imageView.setImageBitmap(bitmap)
//                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun downloadImageAndSetBitmap(info: MediaFileInfo, image: ImageView) {
        withContext(Dispatchers.Main) {
            Glide.with(image)
                .load(
                    appComponent.retrofit.baseUrl().url()
                        .toString() + "media_storage/media/storage/get/image/families/${info.familyId}/types/${info.type}/ids/${info.id}"
                )
                .into(image)
        }
    }
}