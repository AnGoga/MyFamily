package com.angogasapps.myfamily.ui.screens.family_storage

import androidx.annotation.NonNull
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
import io.reactivex.internal.util.ArrayListSupplier
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class StorageManager private constructor() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    var list: ArrayList<StorageObject> = ArrayList()
    @Inject
    lateinit var storageService: FamilyStorageService

    init {
        appComponent.inject(this)
    }

    suspend fun getData(node: String): Boolean {
        val res = storageService.getStorageContent(node)
        when(res) {
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

    fun getListByStack(stack: Stack<String>): ArrayList<StorageObject>{
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


        companion object {
        private var manager: StorageManager? = null
        fun getInstance(): StorageManager {
            synchronized(StorageManager::class.java) {
                if (manager == null) {
                    manager = StorageManager()
                }
                return manager!!
            }
        }
    }
}

