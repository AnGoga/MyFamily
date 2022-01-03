package com.angogasapps.myfamily.di.modules.network

import android.app.Application
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.di.annotations.ServerIp
import com.angogasapps.myfamily.di.annotations.ServerPort
import com.angogasapps.myfamily.di.annotations.StompBuyList
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

    @Singleton
    @StompBuyList
    @Provides
    fun provideStompBuyList(@ServerIp ip: String, @ServerPort port: String): StompClient {
        return Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            "ws://${ip}:8091/websockets/buy_lists"
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
}