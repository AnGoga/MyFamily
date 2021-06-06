package com.angogasapps.myfamily.ui.screens.family_storage

import com.angogasapps.myfamily.models.storage.StorageObject
import io.reactivex.internal.util.ArrayListSupplier

class StorageManager private constructor(){
    var list: ArrayList<StorageObject> = ArrayList()




    companion object{
        private var manager: StorageManager? = null
        fun getInstance(): StorageManager{
            synchronized(StorageManager::class.java) {
                if (manager == null)
                    manager = StorageManager()
                return manager!!
            }
        }
    }
}