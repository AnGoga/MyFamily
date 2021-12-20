package com.angogasapps.myfamily.di.modules

import com.angogasapps.myfamily.database.AppDatabase
import com.angogasapps.myfamily.database.DairyDao
import com.angogasapps.myfamily.ui.screens.personal_dairy.DairyDatabaseManager
import com.angogasapps.myfamily.ui.screens.personal_dairy.PersonalDairyViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DairyModule {

    @Singleton
    @Provides
    fun provideDairyDatabaseManager(
            dairyDao: DairyDao,
            viewModel: PersonalDairyViewModel
    ) = DairyDatabaseManager(dairyDao, viewModel)
}