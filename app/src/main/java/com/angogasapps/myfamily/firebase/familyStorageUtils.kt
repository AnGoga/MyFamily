package com.angogasapps.myfamily.firebase

import android.net.Uri
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*
import com.angogasapps.myfamily.models.storage.TYPE_FILE
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

fun createFile(name: String, value: String, rootNode: String, rootFolder: String, onSuccess: () -> Unit = {}, onError: () -> Unit = {}, key_: String? = null){
    val ref = DATABASE_ROOT.child(rootNode).child(USER.family).child(rootFolder).child(CHILD_VALUE)
    val key = key_ ?: ref.push().key!!

    val map = mapOf(Pair(CHILD_NAME, name), Pair(CHILD_VALUE, value), Pair(CHILD_TYPE, TYPE_FILE))
    ref.child(key).updateChildren(map).addOnCompleteListener { task ->
        if (task.isSuccessful){
            onSuccess()
        }else{
            onError()
        }
    }
}

fun createImageFile(name: String = "", rootNode: String, value: Uri, rootFolder: String, onSuccess: () -> Unit = {}, onError: () -> Unit = {}){
    val ref = DATABASE_ROOT.child(rootNode).child(USER.family).child(rootFolder).child(CHILD_VALUE)
    val key = ref.push().key!!

    val path = STORAGE_ROOT.child(NODE_IMAGE_STORAGE).child(USER.family).child(key)

    path.putFile(value).addOnCompleteListener {
        if (it.isSuccessful){
            path.downloadUrl.addOnCompleteListener { it1 ->
                if (it1.isSuccessful){
                    val url = it1.result.toString()

                    createFile(name = name, value = url, rootNode = rootNode, rootFolder = rootFolder, key_ = key, onSuccess = onSuccess, onError = onError)

                }else{
                    onError()
                }
            }
        }else{
            onError()
        }
    }
}
