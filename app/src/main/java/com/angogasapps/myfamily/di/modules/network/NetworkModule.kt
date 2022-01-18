package com.angogasapps.myfamily.di.modules.network

import android.app.Application
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.di.annotations.ServerIp
import com.angogasapps.myfamily.di.annotations.ServerPort
import com.angogasapps.myfamily.di.annotations.StompBuyList
import com.angogasapps.myfamily.di.annotations.StompChat
import com.angogasapps.myfamily.network.retrofit.apiInterfaces.BuyListAPI
import com.angogasapps.myfamily.network.retrofit.apiInterfaces.ChatAPI
import com.angogasapps.myfamily.network.retrofit.apiInterfaces.MediaStorageAPI
import com.angogasapps.myfamily.utils.moshi.adapters.ListAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    @ServerIp
    fun provideServerIp(app: Application): String {
        return app.getString(R.string.server_api_gateway_ip)
    }

    @Singleton
    @Provides
    @ServerPort
    fun provideServerPort(app: Application): String {
        return app.getString(R.string.server_api_gateway_port)
    }

    @Provides
    @Singleton
    @StompBuyList
    fun provideStompBuyList(@ServerIp ip: String, @ServerPort port: String): StompClient {
        return Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            "ws://${ip}:8091/websockets/buy_lists"
        )
    }

    @Provides
    @Singleton
    @StompChat
    fun provideChatStomp(@ServerIp ip: String, @ServerPort port: String): StompClient {
        return Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            "ws://${ip}:8093/chat/websockets/endpoint"
        )
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(ListAdapter()).build()
    }

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {
        return OkHttpClient().newBuilder().build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        @ServerIp ip: String,
        @ServerPort port: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://${ip}:${port}/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideBuyListApiInterface(retrofit: Retrofit): BuyListAPI {
        return retrofit.create(BuyListAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideChatApiInterface(retrofit: Retrofit): ChatAPI {
        return retrofit.create(ChatAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideMediaStorageApiInterface(retrofit: Retrofit): MediaStorageAPI {
        return retrofit.create(MediaStorageAPI::class.java)
    }

}