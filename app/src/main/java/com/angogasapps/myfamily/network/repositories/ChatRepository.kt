package com.angogasapps.myfamily.network.repositories

import android.net.Uri
import com.angogasapps.myfamily.network.interfaces.ChatSender
import com.angogasapps.myfamily.network.interfaces.ChatService
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ChatRepository @Inject constructor(private val chatService: ChatService): ChatSender by chatService {

}