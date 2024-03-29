package com.angogasapps.myfamily.di.modules.network

import android.app.Application
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.di.annotations.ServerIp
import com.angogasapps.myfamily.di.annotations.ServerPort
import com.angogasapps.myfamily.di.annotations.StompBuyList
import com.angogasapps.myfamily.di.annotations.StompChat
import com.angogasapps.myfamily.network.retrofit.apiInterfaces.BuyListAPI
import com.angogasapps.myfamily.network.retrofit.apiInterfaces.ChatAPI
import com.angogasapps.myfamily.network.retrofit.apiInterfaces.FamilyStorageAPI
import com.angogasapps.myfamily.network.retrofit.apiInterfaces.MediaStorageAPI
import com.angogasapps.myfamily.utils.moshi.adapters.ListAdapter
import com.angogasapps.myfamily.utils.moshi.adapters.StorageObjectAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor


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
//            "ws://${ip}:8091/websockets/buy_lists"
            "ws://${ip}:${port}/websockets/buy_lists"
        )
    }

    @Provides
    @Singleton
    @StompChat
    fun provideChatStomp(@ServerIp ip: String, @ServerPort port: String): StompClient {
        return Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
//            "ws://${ip}:8093/chat/websockets/endpoint"
            "ws://${ip}:${port}/chat/websockets/endpoint"
        )
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(ListAdapter())
            .add(KotlinJsonAdapterFactory())
            .add(StorageObjectAdapter())
            .build()
    }

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }

        val builder = OkHttpClient().newBuilder()
        builder
            .addInterceptor(interceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS) // write timeout
            .readTimeout(15, TimeUnit.SECONDS); // read timeout
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        @ServerIp ip: String,
        @ServerPort port: String,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://${ip}:${port}/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
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

    @Provides
    @Singleton
    fun provideFamilyStorageAPI(retrofit: Retrofit): FamilyStorageAPI {
        return retrofit.create(FamilyStorageAPI::class.java)
    }

}