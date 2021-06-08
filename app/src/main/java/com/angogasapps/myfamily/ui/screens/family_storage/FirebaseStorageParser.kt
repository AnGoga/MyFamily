package com.angogasapps.myfamily.ui.screens.family_storage

import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*
import com.angogasapps.myfamily.models.storage.*
import com.angogasapps.myfamily.utils.asString
import com.google.firebase.database.DataSnapshot

class FirebaseStorageParser private constructor(){

    companion object {
        fun parse(snapshot: DataSnapshot): ArrayList<StorageObject> {
            val list = ArrayList<StorageObject>()
            return parse1(snapshot)
        }
        /*
        //        private fun parse(snapshot: DataSnapshot, list: ArrayList<StorageObject>): ArrayList<StorageObject>{
//            for (child: DataSnapshot in snapshot.children){
//                val type = child.child(CHILD_TYPE).asString()
//
//                val key = child.key!!
//                val name = child.child(CHILD_NAME).asString()!!
//
//                when(type){
//                    TYPE_FILE -> {
//                        val value = child.child(CHILD_VALUE).asString()!!
//                        val file = File(id = key, name = name, value = value)
//                        list.add(file)
//                    }
//                    TYPE_FOLDER -> {
//                        val folder = ArrayFolder(key, name, parse(snapshot, ArrayList<StorageObject>()))
//                        list.add(folder)
//                    }
//                }
//            }
//            return list
//        }
//
//    }
        */
        private fun parse1(snapshot: DataSnapshot): ArrayList<StorageObject> {
            val rootFolder: StorageObject = parseStorageObject(snapshot, snapshot.child(CHILD_BASE_FOLDER))

            val list = (rootFolder as ArrayFolder).value

            return list
        }

        private fun parseStorageObject(root: DataSnapshot, snapshot: DataSnapshot): StorageObject{
            var obj: StorageObject? = null

            val id = snapshot.key!!
            val type = snapshot.child(CHILD_TYPE).asString()!!
            val name = snapshot.child(CHILD_NAME).asString()!!

            if (type == TYPE_FILE){
                val value = snapshot.child(CHILD_VALUE).asString()!!
                obj = File(id, name, value)
            }else if (type == TYPE_FOLDER){
                val value = ArrayList<StorageObject>()
                for (child in snapshot.children){
                    value.add(parseStorageObject(root, root.child(child.key!!)))
                }
                obj = ArrayFolder(id, name, value)
            }
            return obj!!
        }

    }
}