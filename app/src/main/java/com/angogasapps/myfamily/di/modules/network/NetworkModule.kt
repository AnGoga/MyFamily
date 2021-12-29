package com.angogasapps.myfamily.di.modules.network

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
    @StompBuyList
    @Provides
    fun provideStompBuyList(): StompClient {
        return Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            "ws://192.168.1.6:8091/websockets/buy_lists"
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
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.6:8760/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
    }
}