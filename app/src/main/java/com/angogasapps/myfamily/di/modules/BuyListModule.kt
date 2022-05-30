package com.angogasapps.myfamily.di.modules

import com.angogasapps.myfamily.network.firebaseImpl.buy_list.FirebaseBuyListListenerImpl
import com.angogasapps.myfamily.network.firebaseImpl.buy_list.FirebaseBuyListServiceImpl
import com.angogasapps.myfamily.network.interfaces.buy_list.BuyListListener
import com.angogasapps.myfamily.network.interfaces.buy_list.BuyListService
import com.angogasapps.myfamily.network.springImpl.buy_list.SpringBuyListListenerImpl
import com.angogasapps.myfamily.network.springImpl.buy_list.SpringBuyListServiceImpl
import dagger.Binds
import dagger.Module

@Module(includes = [BuyListModule.BuyListModuleBinds::class])
class BuyListModule {
//    @Provides
//    @Singleton
//    fun provideBuyListService(service: FirebaseBuyListServiceImpl): BuyListService = service
//
//    @Provides
//    @Singleton
//    fun provideBuyListListener(listener: FirebaseBuyListListenerImpl): BuyListListener = listener


    @Module
    interface BuyListModuleBinds {
        @Binds
        fun bindBuyListService(service: FirebaseBuyListServiceImpl): BuyListService

        @Binds
        fun bindBuyListListener(listener: FirebaseBuyListListenerImpl): BuyListListener
    }
}