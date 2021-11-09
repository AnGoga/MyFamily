package com.angogasapps.myfamily.di.components

import com.angogasapps.myfamily.database.DairyDao
import com.angogasapps.myfamily.database.MessageDao
import com.angogasapps.myfamily.di.modules.AppModule
import com.angogasapps.myfamily.network.repositories.BuyListRepository
import com.angogasapps.myfamily.network.repositories.FamilyRepository
import com.angogasapps.myfamily.objects.ChatVoicePlayer
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListFragment
import com.angogasapps.myfamily.ui.screens.buy_list.ListOfBuyListsFragment
import com.angogasapps.myfamily.ui.screens.buy_list.dialogs.AddBuyListDialog
import com.angogasapps.myfamily.ui.screens.buy_list.dialogs.BuyListProductCreatorDialog
import com.angogasapps.myfamily.ui.screens.buy_list.dialogs.ChangeOrDeleteBuyListDialog
import com.angogasapps.myfamily.ui.screens.buy_list.dialogs.ChangeOrDeleteProductDialog
import com.angogasapps.myfamily.ui.screens.chat.ChatManager
import com.angogasapps.myfamily.ui.screens.chat.holders.ImageMessageHolder
import com.angogasapps.myfamily.ui.screens.family_storage.CreateImageFileActivity
import com.angogasapps.myfamily.ui.screens.family_storage.StorageActivity
import com.angogasapps.myfamily.ui.screens.family_storage.StorageManager
import com.angogasapps.myfamily.ui.screens.family_storage.StorageNoteBuilderActivity
import com.angogasapps.myfamily.ui.screens.family_storage.gallery_activity.MediaGalleryStorageActivity
import com.angogasapps.myfamily.ui.screens.findorcreatefamily.FindOrCreateFamilyActivity
import com.angogasapps.myfamily.ui.screens.main.MainActivity
import com.angogasapps.myfamily.ui.screens.personal_dairy.DairyBuilderActivity
import com.angogasapps.myfamily.ui.screens.splash.SplashActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: DairyBuilderActivity)
    fun inject(activity: SplashActivity)
    fun inject(manager: ChatManager)
    fun inject(player: ChatVoicePlayer)
    fun inject(holder: ImageMessageHolder)
    fun inject(dialog: BuyListProductCreatorDialog)
    fun inject(dialog: AddBuyListDialog)
    fun inject(dialog: ChangeOrDeleteProductDialog)
    fun inject(dialog: ChangeOrDeleteBuyListDialog)
    fun inject(fragment: ListOfBuyListsFragment)
    fun inject(fragment: BuyListFragment)
    fun inject(activity: FindOrCreateFamilyActivity)
    fun inject(activity: StorageActivity)
    fun inject(activity: StorageNoteBuilderActivity)
    fun inject(activity: CreateImageFileActivity)
    fun inject(activity: MediaGalleryStorageActivity)
    fun inject(manager: StorageManager)


    val familyRepository: FamilyRepository
    val messageDao: MessageDao
    val dairyDao: DairyDao
}