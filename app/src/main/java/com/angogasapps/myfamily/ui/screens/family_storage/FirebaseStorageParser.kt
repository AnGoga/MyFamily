package com.angogasapps.myfamily.ui.screens.family_storage

import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*
import com.angogasapps.myfamily.models.storage.*
import com.angogasapps.myfamily.utils.asString
import com.google.firebase.database.DataSnapshot

class FirebaseStorageParser private constructor(){

    companion object {
        fun parse(snapshot: DataSnapshot): ArrayList<StorageObject> {
            val list = ArrayList<StorageObject>()
            parse(snapshot, list)
            return list
        }


        private fun parse(snapshot: DataSnapshot, list: ArrayList<StorageObject>): ArrayList<StorageObject>{
            for (child: DataSnapshot in snapshot.children){
                val type = child.child(CHILD_TYPE).asString()

                val key = child.key!!
                val name = child.child(CHILD_NAME).asString()!!

                when(type){
                    TYPE_FILE -> {
                        val value = child.child(CHILD_VALUE).asString()!!
                        val file = File(id = key, name = name, value = value)
                        list.add(file)
                    }
                    TYPE_FOLDER -> {
                        val folder = Folder(key, name, parse(snapshot, ArrayList<StorageObject>()))
                        list.add(folder)
                    }
                }
            }
            return list
        }

    }


}