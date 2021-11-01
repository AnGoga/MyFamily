package com.angogasapps.myfamily.di.modules

import com.angogasapps.myfamily.network.firebaseImpl.FirebaseBuyListListenerImpl
import com.angogasapps.myfamily.network.firebaseImpl.FirebaseBuyListServiceImpl
import com.angogasapps.myfamily.network.interfaces.buy_list.BuyListListener
import com.angogasapps.myfamily.network.interfaces.buy_list.BuyListService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BuyListModule {

    @Provides
    @Singleton
    fun provideBuyListService(service: FirebaseBuyListServiceImpl): BuyListService = service

    @Provides
    @Singleton
    fun provideBuyListListener(listener: FirebaseBuyListListenerImpl): BuyListListener = listener
}