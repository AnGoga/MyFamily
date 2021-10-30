package com.angogasapps.myfamily.di.modules

import com.angogasapps.myfamily.network.firebaseImpl.FirebaseChatServiceImpl
import com.angogasapps.myfamily.network.interfaces.ChatService
import com.angogasapps.myfamily.network.interfaces.ChatVoiceGetter
import com.angogasapps.myfamily.network.interfaces.ImageDownloader
import com.angogasapps.myfamily.network.repositories.ChatRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ChatModule {

    @Singleton
    @Provides
    fun provideChatService() : ChatService = FirebaseChatServiceImpl()

    @Singleton
    @Provides
    fun provideChatRepository(
        chatService: ChatService
    ) : ChatRepository = ChatRepository(chatService)

    @Provides
    @Singleton
    fun provideChatVoiceGetter(chatService: ChatService): ChatVoiceGetter = chatService

    @Provides
    @Singleton
    fun provideIMageDownloader(chatService: ChatService): ImageDownloader = chatService
}