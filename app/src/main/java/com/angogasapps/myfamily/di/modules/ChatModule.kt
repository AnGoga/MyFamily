package com.angogasapps.myfamily.di.modules

import com.angogasapps.myfamily.database.MessageDao
import com.angogasapps.myfamily.network.firebaseImpl.chat.FirebaseChatListenerImpl
import com.angogasapps.myfamily.network.firebaseImpl.chat.FirebaseChatServiceImpl
import com.angogasapps.myfamily.network.interfaces.chat.ChatService
import com.angogasapps.myfamily.network.interfaces.chat.ChatVoiceGetter
import com.angogasapps.myfamily.network.interfaces.chat.ImageDownloader
import com.angogasapps.myfamily.network.interfaces.chat.ChatListener
import com.angogasapps.myfamily.network.repositories.ChatRepository
import com.angogasapps.myfamily.network.springImpl.chat.SpringChatListenerImpl
import com.angogasapps.myfamily.network.springImpl.chat.SpringChatServiceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [ChatModule.ChatModuleBinder::class])
class ChatModule {

//    @Singleton
//    @Provides
//    fun provideChatService() : ChatService = SpringChatServiceImpl()//FirebaseChatServiceImpl()
//
    @Singleton
    @Provides
    fun provideChatRepository(
        chatService: ChatService,
        messageDao: MessageDao
    ) : ChatRepository = ChatRepository(chatService, messageDao)

    @Provides
    @Singleton
    fun provideChatVoiceGetter(chatService: ChatService): ChatVoiceGetter = chatService

    @Provides
    @Singleton
    fun provideIMageDownloader(chatService: ChatService): ImageDownloader = chatService

    @Provides
    @Singleton
//    fun provideChatListener(chatListener: FirebaseChatListenerImpl): ChatListener = chatListener
    fun provideChatListener(chatListener: SpringChatListenerImpl): ChatListener = chatListener

    @Module
    interface ChatModuleBinder {
        @Singleton
        @Binds
        fun provideChatService(service: SpringChatServiceImpl) : ChatService

    }


}