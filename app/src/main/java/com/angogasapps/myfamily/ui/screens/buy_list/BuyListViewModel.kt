package com.angogasapps.myfamily.ui.screens.buy_list


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.angogasapps.myfamily.network.repositories.BuyListRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuyListViewModel @Inject constructor(
    private val buyListRepository: BuyListRepository,
    application: Application,
) : AndroidViewModel(application) {
    val buyLists
        get() = buyListRepository.buyLists
}