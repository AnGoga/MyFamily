package com.angogasapps.myfamily.firebase

import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*
import com.angogasapps.myfamily.models.storage.TYPE_FOLDER
import java.util.*

fun createFolder(name: String, rootNode: String, rootFolder: String, onSuccess: () -> Unit = {}, onError: () -> Unit = {}){
    val ref = DATABASE_ROOT.child(rootNode).child(USER.family)
    val key = ref.push().key!!

    ref.child(key).updateChildren(mapOf(Pair(CHILD_NAME, name))).addOnCompleteListener { it1 ->
        if (it1.isSuccessful){
            ref.child(rootFolder).child(CHILD_VALUE).child(key)
                    .updateChildren(mapOf(Pair(CHILD_TYPE, TYPE_FOLDER))).addOnCompleteListener { it2 ->
                if (it2.isSuccessful){
                    onSuccess()
                }else{
                    onError()
                }
            }
        }else{
            onError()
        }
    }
}