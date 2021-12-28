package com.angogasapps.myfamily.di.modules

import com.angogasapps.myfamily.network.firebaseImpl.buy_list.FirebaseBuyListListenerImpl
import com.angogasapps.myfamily.network.firebaseImpl.buy_list.FirebaseBuyListServiceImpl
import com.angogasapps.myfamily.network.interfaces.buy_list.BuyListListener
import com.angogasapps.myfamily.network.interfaces.buy_list.BuyListService
import com.angogasapps.myfamily.network.retrofit.ApiInterfaces.BuyListAPI
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class BuyListModule {
    @Provides
    @Singleton
    fun provideBuyListService(service: FirebaseBuyListServiceImpl): BuyListService = service

    @Provides
    @Singleton
    fun provideBuyListListener(listener: FirebaseBuyListListenerImpl): BuyListListener = listener

    @Provides
    @Singleton
    fun provideBuyListApiInterface(retrofit: Retrofit) : BuyListAPI {
        return retrofit.create(BuyListAPI::class.java)
    }
}