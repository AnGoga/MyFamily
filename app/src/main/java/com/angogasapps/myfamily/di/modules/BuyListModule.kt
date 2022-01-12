package com.angogasapps.myfamily.di.modules

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
        fun bindBuyListService(service: SpringBuyListServiceImpl): BuyListService

        @Binds
        fun bindBuyListListener(listener: SpringBuyListListenerImpl): BuyListListener
    }
}