package com.angogasapps.myfamily.network.interfaces.family_stoarge

import android.net.Uri
import com.angogasapps.myfamily.models.storage.ArrayFolder
import com.angogasapps.myfamily.models.storage.File
import com.angogasapps.myfamily.models.storage.StorageObject
import com.angogasapps.myfamily.network.Result

interface FamilyStorageService {
    fun createFolder(
        name: String,
        rootNode: String,
        rootFolder: String,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {}
    )

    fun createStorageFile(uri: Uri, name: String, rootFolderId: String)

    fun createFile(
        name: String,
        value: String,
        rootNode: String,
        rootFolder: String,
        onSuccess: (value: String, key: String) -> Unit = { s1, s2 -> },
        onError: () -> Unit = {},
        key_: String? = null
    )

    fun createImageFile(
        name: String = "", rootNode: String, value: Uri, rootFolder: String,
        onSuccess: (value: String, key: String) -> Unit = { s1, s2 -> }, onError: () -> Unit = {}
    )

    fun removeFile(
        file: File, folderId: String, rootNode: String,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {}
    )

    fun removeFolder(folder: ArrayFolder, rootNode: String, rootFolderId: String)

    fun renameFolder(folder: ArrayFolder, newName: String, rootNode: String)

    fun renameFile(
        file: File,
        newName: String,
        rootNode: String,
        folderId: String,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {}
    )

    fun downloadFile(
        file: File,
        onSuccess: (dFile: java.io.File, contentType: String) -> Unit = { it1, it2 -> },
        onError: () -> Unit = {}
    )

    suspend fun getStorageContent(node: String): Result<List<StorageObject>>
}