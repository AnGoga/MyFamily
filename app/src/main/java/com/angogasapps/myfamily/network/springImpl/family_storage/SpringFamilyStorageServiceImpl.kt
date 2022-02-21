package com.angogasapps.myfamily.network.springImpl.family_storage

import android.net.Uri
import androidx.core.net.toFile
import com.angogasapps.myfamily.firebase.USER
import com.angogasapps.myfamily.models.storage.ArrayFolder
import com.angogasapps.myfamily.models.storage.File
import com.angogasapps.myfamily.models.storage.StorageObject
import com.angogasapps.myfamily.network.Result
import com.angogasapps.myfamily.network.interfaces.family_stoarge.FamilyStorageService
import com.angogasapps.myfamily.network.retrofit.apiInterfaces.FamilyStorageAPI
import com.angogasapps.myfamily.network.springImpl.media_storage.MediaStorageService
import com.angogasapps.myfamily.network.spring_models.family_storage.CreateFileRequest
import com.angogasapps.myfamily.network.spring_models.family_storage.CreateFolderRequest
import com.angogasapps.myfamily.network.spring_models.media_storage.EMediaType
import com.angogasapps.myfamily.network.spring_models.media_storage.MediaFileInfo
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpringFamilyStorageServiceImpl @Inject constructor(
    private val api: FamilyStorageAPI,
    private val mediaStorageService: MediaStorageService
) : FamilyStorageService {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun createFolder(
        name: String,
        rootNode: String,
        rootFolder: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        scope.launch {
            val response = api.createFolder(
                familyId = USER.family,
                storageType = rootNode,
                request = CreateFolderRequest(
                    name = name,
                    rootNode = rootNode,
                    rootFolder = rootFolder
                )
            )
            if (response.isSuccessful) {

            } else {
                System.err.println("createFolder ERROR: code ${response.code()} , error message" + response.errorBody())
            }
        }
    }

    override fun createStorageFile(uri: Uri, name: String, rootFolderId: String) {
        scope.launch {
            mediaStorageService.uploadFile(
                file = uri.toFile(),
                fileInfo = MediaFileInfo(type = EMediaType.STORAGE_FILE, familyId = USER.family),
                request = CreateFileRequest(name = name, rootFolder = rootFolderId, value = "", rootNode = EMediaType.STORAGE_FILE.toString())
            )
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
        TODO("Not yet implemented")
    }

    override fun createImageFile(
        name: String,
        rootNode: String,
        value: Uri,
        rootFolder: String,
        onSuccess: (value: String, key: String) -> Unit,
        onError: () -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun removeFile(
        file: File,
        folderId: String,
        rootNode: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun removeFolder(folder: ArrayFolder, rootNode: String, rootFolderId: String) {
        TODO("Not yet implemented")
    }

    override fun renameFolder(folder: ArrayFolder, newName: String, rootNode: String) {
        TODO("Not yet implemented")
    }

    override fun renameFile(
        file: File,
        newName: String,
        rootNode: String,
        folderId: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun downloadFile(
        file: File,
        onSuccess: (dFile: java.io.File, contentType: String) -> Unit,
        onError: () -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getStorageContent(node: String): Result<ArrayList<StorageObject>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getContent(USER.family, storageType = node)
                return@withContext Result.Success(response.value)
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext Result.Error(e)
            }

        }
    }
}