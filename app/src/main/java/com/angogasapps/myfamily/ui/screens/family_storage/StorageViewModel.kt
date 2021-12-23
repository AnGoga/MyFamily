package com.angogasapps.myfamily.ui.screens.family_storage

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.models.storage.ArrayFolder
import com.angogasapps.myfamily.models.storage.StorageObject
import com.angogasapps.myfamily.network.Result
import com.angogasapps.myfamily.network.interfaces.family_stoarge.FamilyStorageService
import com.angogasapps.myfamily.utils.toMapFolder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Singleton
class StorageViewModel @Inject constructor(
    private val storageService: FamilyStorageService,
    application: Application
) : AndroidViewModel(application) {

    var list: ArrayList<StorageObject> = ArrayList()


    suspend fun getData(node: String): Boolean {
        val res = storageService.getStorageContent(node)
        when (res) {
            is Result.Error -> return false
            is Result.Success -> {
                list = res.data
                return true
            }
        }
    }

    /*
    DATABASE_ROOT.child(node).child(USER.family)
                .addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                        error.toException().printStackTrace()
                        this@callbackFlow.trySendBlocking(false)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        println(snapshot.toString())
                        try {
                            list = FirebaseStorageParser.parse(snapshot)
//                            map = list.toMapFolder()
                            this@callbackFlow.trySendBlocking(true)
                        }catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })
        awaitClose {  }
     */

    fun getListByStack(stack: Stack<String>): ArrayList<StorageObject> {
        var result: ArrayList<StorageObject> = list
        for (id: String in stack) {
            for (storageObj in result) {
                if (storageObj.id == id) {
                    result = (storageObj as ArrayFolder).value
                    break
                }
            }
        }

        return result
    }
}

