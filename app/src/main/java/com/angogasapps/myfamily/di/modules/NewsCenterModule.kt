package com.angogasapps.myfamily.di.modules

import com.angogasapps.myfamily.network.firebaseImpl.news_center.FirebaseNewsCenterListenerImpl
import com.angogasapps.myfamily.network.firebaseImpl.news_center.FirebaseNewsCenterServiceImpl
import com.angogasapps.myfamily.network.interfaces.news_center.NewsCenterListener
import com.angogasapps.myfamily.network.interfaces.news_center.NewsCenterService
import dagger.Binds
import dagger.Module

@Module
abstract class NewsCenterModule {
    @Binds
    abstract fun bindNewsCenterService(service: FirebaseNewsCenterServiceImpl): NewsCenterService

    @Binds
    abstract fun bindNewsCenterListener(listener: FirebaseNewsCenterListenerImpl): NewsCenterListener
}