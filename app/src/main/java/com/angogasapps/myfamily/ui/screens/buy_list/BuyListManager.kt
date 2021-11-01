package com.angogasapps.myfamily.ui.screens.buy_list


import kotlin.jvm.Volatile
import com.angogasapps.myfamily.models.buy_list.BuyList
import io.reactivex.subjects.PublishSubject
import com.angogasapps.myfamily.models.buy_list.BuyListEvent
import com.google.firebase.database.ChildEventListener
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import com.google.firebase.database.DataSnapshot
import com.angogasapps.myfamily.utils.BuyListUtils
import com.google.firebase.database.DatabaseError
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.models.buy_list.BuyList.Product
import com.angogasapps.myfamily.network.interfaces.buy_list.BuyListListener
import com.angogasapps.myfamily.network.interfaces.buy_list.BuyListService
import com.angogasapps.myfamily.network.repositories.BuyListRepository
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListManager
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuyListManager @Inject constructor(
    private val buyListRepository: BuyListRepository,
) {
    val buyLists
        get() = buyListRepository.buyLists
}