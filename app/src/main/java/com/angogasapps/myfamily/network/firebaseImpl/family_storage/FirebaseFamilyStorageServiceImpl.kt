package com.angogasapps.myfamily.network.firebaseImpl.family_storage

import android.net.Uri
import android.os.Environment
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.models.storage.*
import com.angogasapps.myfamily.network.Result
import com.angogasapps.myfamily.network.interfaces.family_stoarge.FamilyStorageService
import com.angogasapps.myfamily.ui.screens.family_storage.FirebaseStorageParser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseFamilyStorageServiceImpl @Inject constructor() : FamilyStorageService {

    override fun createFolder(
        name: String,
        rootNode: String,
        rootFolder: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val ref = DATABASE_ROOT.child(rootNode).child(USER.family)
        val key = ref.push().key!!

        ref.child(key).updateChildren(mapOf(Pair(CHILD_NAME, name))).addOnCompleteListener { it1 ->
            if (it1.isSuccessful) {
                ref.child(rootFolder).child(CHILD_VALUE).child(key)
                    .updateChildren(mapOf(Pair(CHILD_TYPE, TYPE_FOLDER)))
                    .addOnCompleteListener { it2 ->
                        if (it2.isSuccessful) {
                            onSuccess()
                        } else {
                            onError()
                        }
                    }
            } else {
                onError()
            }
        }
    }

    override fun createStorageFile(uri: Uri, name: String, rootFolderId: String) {
        val key = DATABASE_ROOT.child(NODE_FILE_STORAGE).child(USER.family).child(rootFolderId)
            .push().key!!
        val path =
            STORAGE_ROOT.child(NODE_FILE_STORAGE).child(USER.family).child(rootFolderId).child(key)

        path.putFile(uri).addOnCompleteListener {
            if (it.isSuccessful) {
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

    override fun createFile(
        name: String,
        value: String,
        rootNode: String,
        rootFolder: String,
        onSuccess: (value: String, key: String) -> Unit,
        onError: () -> Unit,
        key_: String?
    ) {

        val ref = DATABASE_ROOT.child(rootNode).child(USER.family).child(rootFolder).child(
            CHILD_VALUE
        )
        val key = key_ ?: ref.push().key!!

        val map =
            mapOf(Pair(CHILD_NAME, name), Pair(CHILD_VALUE, value), Pair(CHILD_TYPE, TYPE_FILE))
        ref.child(key).updateChildren(map).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(value, key)
            } else {
                onError()
            }
        }
    }

    override fun createImageFile(
        name: String, rootNode: String, value: Uri, rootFolder: String,
        onSuccess: (value: String, key: String) -> Unit, onError: () -> Unit
    ) {

        val ref = DATABASE_ROOT.child(rootNode).child(USER.family).child(rootFolder).child(
            CHILD_VALUE
        )
        val key = ref.push().key!!

        val path = STORAGE_ROOT.child(NODE_IMAGE_STORAGE).child(USER.family).child(key)

        path.putFile(value).addOnCompleteListener {
            if (it.isSuccessful) {
                path.downloadUrl.addOnCompleteListener { it1 ->
                    if (it1.isSuccessful) {
                        val url = it1.result.toString()

                        createFile(
                            name = name,
                            value = url,
                            rootNode = rootNode,
                            rootFolder = rootFolder,
                            key_ = key,
                            onSuccess = onSuccess,
                            onError = onError
                        )

                    } else {
                        onError()
                    }
                }
            } else {
                onError()
            }
        }
    }

    override fun removeFile(
        file: File,
        folderId: String,
        rootNode: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
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
        if (rootNode != NODE_NOTE_STORAGE) {

            FirebaseStorage.getInstance().getReferenceFromUrl(file.value).delete()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        function()
                    } else {
                        onError()
                    }
                }

        } else {
            function()
        }
    }

    override fun removeFolder(folder: ArrayFolder, rootNode: String, rootFolderId: String) {
        if (folder.value.isEmpty()) {
            removeEmptyFolder(folder, rootNode, rootFolderId)
        }

        folder.value.forEachIndexed { index, obj ->
            if (obj.isFile()) {
                removeFile(obj as File, folder.id, rootNode)
            } else if (obj.isFolder()) {
                removeFolder(obj as ArrayFolder, rootNode, folder.id)
            }
        }
        (folder.value).clear()
        removeEmptyFolder(folder, rootNode, rootFolderId)
    }

    private fun removeEmptyFolder(folder: ArrayFolder, rootNode: String, rootFolderId: String) {
        if (folder.value.isNotEmpty()) {
            removeFolder(folder, rootNode, rootFolderId)
            return
        }
        val baseRef = DATABASE_ROOT.child(rootNode).child(USER.family)

        baseRef.child(rootFolderId).child(CHILD_VALUE).child(folder.id).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    baseRef.child(folder.id).removeValue()
                }
            }
    }

    override fun renameFolder(folder: ArrayFolder, newName: String, rootNode: String) {
        DATABASE_ROOT.child(rootNode).child(USER.family).child(folder.id)
            .updateChildren(mapOf(Pair(CHILD_NAME, newName))).addOnCompleteListener {
                if (it.isSuccessful) {

                } else {

                }
            }
    }

    override fun renameFile(
        file: File,
        newName: String,
        rootNode: String,
        folderId: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        DATABASE_ROOT.child(rootNode).child(USER.family).child(folderId).child(CHILD_VALUE)
            .child(file.id).child(
                CHILD_NAME
            )
            .setValue(newName).addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onError()
                }
            }
    }

    override fun downloadFile(
        file: File,
        onSuccess: (dFile: java.io.File, contentType: String) -> Unit,
        onError: () -> Unit
    ) {
        val path = FirebaseStorage.getInstance().getReferenceFromUrl(file.value)

        path.metadata.addOnCompleteListener { metadata ->
            if (metadata.isSuccessful) {
                val contentType = metadata.result.contentType ?: ""
                val dFile = java.io.File(
                    AppApplication.app.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                    """${file.name}.${contentType.split("/")[1]}"""
                )
                path.getFile(dFile).addOnCompleteListener {
                    if (it.isSuccessful) {
                        onSuccess(dFile, contentType)
                    } else {
                        onError()
                    }
                }
            } else {
                onError()
            }
        }
    }

    override suspend fun getStorageContent(node: String): Result<List<StorageObject>> {
        try {
            val res = DATABASE_ROOT.child(node).child(USER.family).get().await()
                ?: throw Exception("resultat of download family storage is null !!!")
            println(res.toString())
            val list = FirebaseStorageParser.parse(res)
            return Result.Success(list)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(e)
        }
    }
}