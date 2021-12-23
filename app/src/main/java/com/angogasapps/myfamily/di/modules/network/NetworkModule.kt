package com.angogasapps.myfamily.di.modules.network

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideStomp(): StompClient {
        return Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://localhost:8091/websockets/buy_lists");
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }
}