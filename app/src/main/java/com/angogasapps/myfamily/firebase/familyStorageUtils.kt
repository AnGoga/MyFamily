package com.angogasapps.myfamily.firebase

import android.net.Uri
import android.os.Environment
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*
import com.angogasapps.myfamily.models.storage.ArrayFolder
import com.angogasapps.myfamily.models.storage.File
import com.angogasapps.myfamily.models.storage.TYPE_FILE
import com.angogasapps.myfamily.models.storage.TYPE_FOLDER
import com.google.firebase.storage.FirebaseStorage

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

fun createStorageFile(uri: Uri, name: String, rootFolderId: String){
    val key = DATABASE_ROOT.child(NODE_FILE_STORAGE).child(USER.family).child(rootFolderId).push().key!!
    val path = STORAGE_ROOT.child(NODE_FILE_STORAGE).child(USER.family).child(rootFolderId).child(key)

    path.putFile(uri).addOnCompleteListener {
        if (it.isSuccessful){
            path.downloadUrl.addOnCompleteListener { it2 ->
                createFile(
                        name = name,
                        value = it2.result.toString(),
                        rootNode = NODE_FILE_STORAGE,
                        rootFolder = rootFolderId
                )
            }
        }
    }
}

fun createFile(name: String, value: String, rootNode: String, rootFolder: String,
               onSuccess: (value: String, key: String) -> Unit = {s1, s2 -> }, onError: () -> Unit = {}, key_: String? = null){

    val ref = DATABASE_ROOT.child(rootNode).child(USER.family).child(rootFolder).child(CHILD_VALUE)
    val key = key_ ?: ref.push().key!!

    val map = mapOf(Pair(CHILD_NAME, name), Pair(CHILD_VALUE, value), Pair(CHILD_TYPE, TYPE_FILE))
    ref.child(key).updateChildren(map).addOnCompleteListener { task ->
        if (task.isSuccessful){
            onSuccess(value, key)
        }else{
            onError()
        }
    }
}

fun createImageFile(name: String = "", rootNode: String, value: Uri, rootFolder: String,
                    onSuccess: (value: String, key: String) -> Unit = {s1, s2 ->}, onError: () -> Unit = {}){

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

fun removeFile(file: File, folderId: String, rootNode: String, onSuccess: () -> Unit = {}, onError: () -> Unit = {}){
    val function = {
        DATABASE_ROOT.child(rootNode).child(USER.family).child(folderId).child(CHILD_VALUE)
                .child(file.id).removeValue().addOnCompleteListener {
                    if (it.isSuccessful) {
                        onSuccess()
                    } else {
                        onError()
                    }
                }
    }

//    if (rootNode == NODE_FILE_STORAGE || rootNode == NODE_IMAGE_STORAGE) {
    if (rootNode != NODE_NOTE_STORAGE) {

        FirebaseStorage.getInstance().getReferenceFromUrl(file.value).delete().addOnCompleteListener {
            if (it.isSuccessful) {
                function()
            } else {
                onError()
            }
        }

    }else{
        function()
    }
}

fun removeFolder(folder: ArrayFolder, rootNode: String, rootFolderId: String){
    if (folder.value.isEmpty()){
        removeEmptyFolder(folder, rootNode, rootFolderId)
    }

    folder.value.forEachIndexed { index, obj ->
        if (obj.isFile()){
            removeFile(obj as File, folder.id, rootNode)
        }else if (obj.isFolder()){
            removeFolder(obj as ArrayFolder, rootNode, folder.id)
        }
    }
    folder.value.clear()
    removeEmptyFolder(folder, rootNode, rootFolderId)
}

private fun removeEmptyFolder(folder: ArrayFolder, rootNode: String, rootFolderId: String){
    if (folder.value.isNotEmpty()){
        removeFolder(folder, rootNode, rootFolderId)
        return
    }
    val baseRef = DATABASE_ROOT.child(rootNode).child(USER.family)

    baseRef.child(rootFolderId).child(CHILD_VALUE).child(folder.id).removeValue().addOnCompleteListener {
        if (it.isSuccessful) {
            baseRef.child(folder.id).removeValue()
        }
    }
}

fun renameFolder(folder: ArrayFolder, newName: String, rootNode: String){
    DATABASE_ROOT.child(rootNode).child(USER.family).child(folder.id)
      .updateChildren(mapOf(Pair(CHILD_NAME, newName))).addOnCompleteListener {
          if (it.isSuccessful){

          }else{

          }
      }
}

fun renameFile(file: File, newName: String, rootNode: String, folderId: String, onSuccess: () -> Unit = {}, onError: () -> Unit = {}){
    DATABASE_ROOT.child(rootNode).child(USER.family).child(folderId).child(CHILD_VALUE).child(file.id).child(CHILD_NAME)
      .setValue(newName).addOnCompleteListener {
          if (it.isSuccessful){
              onSuccess()
          }else {
              onError()
          }
      }
}

fun downloadFile(file: File, onSuccess: (dFile: java.io.File, contentType: String) -> Unit = {it1, it2 ->}, onError: () -> Unit = {}){
    val path = FirebaseStorage.getInstance().getReferenceFromUrl(file.value)

    path.metadata.addOnCompleteListener { metadata ->
        if (metadata.isSuccessful){
            val contentType = metadata.result.contentType?:""
            val dFile = java.io.File(
                    AppApplication.getInstance().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                    """${file.name}.${contentType.split("/")[1]}"""
            )
            path.getFile(dFile).addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess(dFile, contentType)
                } else{ onError() }
            }
        } else { onError() }
    }
}






