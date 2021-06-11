package com.angogasapps.myfamily.ui.screens.family_storage

import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*
import com.angogasapps.myfamily.models.storage.*
import com.angogasapps.myfamily.utils.asString
import com.google.firebase.database.DataSnapshot

class FirebaseStorageParser private constructor(){

    companion object {
/*
        fun parse(snapshot: DataSnapshot): ArrayList<StorageObject> {
            val list = ArrayList<StorageObject>()
            return parse1(snapshot)
        }

        private fun parse1(snapshot: DataSnapshot): ArrayList<StorageObject> {
            val rootFolder: StorageObject = parseStorageObject(snapshot, snapshot.child(CHILD_BASE_FOLDER))

            val list = (rootFolder as ArrayFolder).value

            return list
        }

        private fun parseStorageObject(root: DataSnapshot, snapshot: DataSnapshot): StorageObject{
            var obj: StorageObject? = null

            val id = snapshot.key!!
            val type = snapshot.child(CHILD_TYPE).asString()!!
            val name = snapshot.child(CHILD_NAME).asString()?:""

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
        }*/


        fun parse(snapshot: DataSnapshot): ArrayList<StorageObject> {
            return parseFolder(snapshot, snapshot.child(CHILD_BASE_FOLDER)).value
        }

        private fun parseFolder(root: DataSnapshot, snapshot: DataSnapshot): ArrayFolder{
            val id = snapshot.key!!
            val name = snapshot.child(CHILD_NAME).asString()?:""
            val value = ArrayList<StorageObject>()

            for (child in snapshot.child(CHILD_VALUE).children){

                when(child.child(CHILD_TYPE).asString()!!){
                    TYPE_FOLDER -> {
                        value.add(parseFolder(root, root.child(child.key!!)))
                    }
                    TYPE_FILE -> {
                        value.add(parseFile(child))
                    }
                }
            }
            return ArrayFolder(id = id, name = name, value = value)
        }

        private fun parseFile(snapshot: DataSnapshot): File{
            val id = snapshot.key!!
            val name = snapshot.child(CHILD_NAME).asString()?:""
            val value = snapshot.child(CHILD_VALUE).asString()?:""
            return File(id = id, name = name, value = value)
        }
    }
}