package com.angogasapps.myfamily.ui.screens.personal_dairy

import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.database.DatabaseManager
import com.angogasapps.myfamily.models.DairyObject
import com.angogasapps.myfamily.utils.indexOfThisKey
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel

class PersonalDairyManager private constructor(){
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    var list: ArrayList<DairyObject> = ArrayList()
    val channel = BroadcastChannel<DairyEvent>(1)

    init {
        scope.launch {
            getDataFromRoom()
        }
    }

    companion object{
        private var manager: PersonalDairyManager? = null
        fun getInstance(): PersonalDairyManager{
            synchronized(PersonalDairyManager::class.java){
                if (manager == null)
                    manager = PersonalDairyManager()
                return manager!!
            }
        }
    }


    fun addDairy(dairy: DairyObject){
        var index = list.indexOfThisKey(dairy)
        if (index >= 0){
            removeDairy(dairy, index)
        }
        list.add(dairy)
        list.sortBy { it.time }
        index = list.indexOf(dairy)


        channel.trySend(DairyEvent(index, EDairyEvents.add, dairy.key))
    }

    fun removeDairy(dairy: DairyObject){
        removeDairy(dairy, list.indexOf(dairy))
    }

    private fun removeDairy(dairy: DairyObject, index: Int){
        list.removeAt(index)
        channel.trySend(DairyEvent(index, EDairyEvents.remove, dairy.key))
    }

    private suspend fun getDataFromRoom() = withContext(Dispatchers.Default){
        val dairyList: List<DairyObject?>? = appComponent.dairyDao.getAll()
        if (dairyList.isNullOrEmpty()) {
            return@withContext
        }

        val arrayList = ArrayList(dairyList.toMutableList())
        arrayList.forEachIndexed { index, it -> if (it == null) arrayList.removeAt(index) }


        list = arrayList as ArrayList<DairyObject>

        list.sortBy { it.time }

        list.forEachIndexed { index, it ->
            channel.trySend(DairyEvent(index, EDairyEvents.add, it.key))
        }
    }
}




data class DairyEvent( val index: Int, val events: EDairyEvents, val id: String)

enum class EDairyEvents{
    add, remove
}