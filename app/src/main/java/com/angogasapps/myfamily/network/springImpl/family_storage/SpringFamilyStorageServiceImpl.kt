package com.angogasapps.myfamily.network.springImpl.family_storage

import android.net.Uri
import android.os.Environment
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.firebase.NODE_CHAT
import com.angogasapps.myfamily.firebase.NODE_IMAGE_STORAGE
import com.angogasapps.myfamily.firebase.USER
import com.angogasapps.myfamily.models.storage.ArrayFolder
import com.angogasapps.myfamily.models.storage.File
import com.angogasapps.myfamily.models.storage.StorageFolder
import com.angogasapps.myfamily.models.storage.StorageObject
import com.angogasapps.myfamily.network.Result
import com.angogasapps.myfamily.network.interfaces.family_stoarge.FamilyStorageService
import com.angogasapps.myfamily.network.retrofit.apiInterfaces.FamilyStorageAPI
import com.angogasapps.myfamily.network.springImpl.media_storage.MediaStorageService
import com.angogasapps.myfamily.network.spring_models.family_storage.CreateFileRequest
import com.angogasapps.myfamily.network.spring_models.family_storage.CreateFolderRequest
import com.angogasapps.myfamily.network.spring_models.media_storage.EMediaType
import com.angogasapps.myfamily.network.spring_models.media_storage.MediaFileInfo
import com.angogasapps.myfamily.utils.RealPathUtil
import com.angogasapps.myfamily.utils.getImageUrlFromMediaFileInfo

import com.squareup.moshi.Moshi
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpringFamilyStorageServiceImpl @Inject constructor(
    private val api: FamilyStorageAPI,
    private val mediaStorageService: MediaStorageService,
    private val familyStorageAPI: FamilyStorageAPI,
    private val moshi: Moshi
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
                file = java.io.File(
                    RealPathUtil.getRealPath(
                        appComponent.context,
                        uri
                    ) ?: ""
                ),//uri.toFile(),//java.io.File(uri.getRealPathFromURI()),
                fileInfo = MediaFileInfo(
                    type = EMediaType.STORAGE_FILE,
                    familyId = USER.family,
                    rootFolder = rootFolderId,
                    name = name,
                    value = ""
                ),
                request = CreateFileRequest(
                    name = name,
                    rootFolder = rootFolderId,
                    value = "",
                    rootNode = EMediaType.STORAGE_FILE.toString()
                )
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
        scope.launch {
            familyStorageAPI.createFile(
                familyId = USER.family,
                storageType = EMediaType.STORAGE_NOTE.toString(),
                MediaFileInfo(
                    id = key_ ?: "",
                    type = EMediaType.STORAGE_NOTE,
                    familyId = USER.family,
                    name = name,
                    rootFolder = rootFolder,
                    value = value
                )
            )
        }
    }

    override fun createImageFile(
        name: String,
        rootNode: String,
        value: Uri,
        rootFolder: String,
        onSuccess: (value: String, key: String) -> Unit,
        onError: () -> Unit
    ) {
        scope.launch {
            try {
                val info = MediaFileInfo(
                    familyId = USER.family,
                    type = if (rootNode == NODE_CHAT) EMediaType.CHAT_IMAGE else EMediaType.STORAGE_IMAGE,
                    rootFolder = rootFolder
                )
                val file = java.io.File(value.path)
//                val extra = CreateFileRequest(rootFolder = rootFolder)
                val loadedInfo = mediaStorageService.uploadFile(file, info)
                withContext(Dispatchers.Main) {
                    onSuccess(
                        getImageUrlFromMediaFileInfo(loadedInfo.fileInfo),
                        loadedInfo.fileInfo.id
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onError()
                }
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
        scope.launch {
            familyStorageAPI.removeFile(
                familyId = USER.family,
                request = MediaFileInfo(id = file.id)
            )
        }
    }

    override fun removeFolder(folder: ArrayFolder, rootNode: String, rootFolderId: String) {
        scope.launch {
            familyStorageAPI.removeFolder(
                familyId = USER.family,
                request = MediaFileInfo(id = folder.id)
            )
        }
    }

    override fun renameFolder(folder: ArrayFolder, newName: String, rootNode: String) {
        renameStorageObject(id = folder.id, newName = newName)
    }

    override fun renameFile(
        file: File,
        newName: String,
        rootNode: String,
        folderId: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        renameStorageObject(id = file.id, newName = newName)
    }


    private fun renameStorageObject(id: String, newName: String) {
        scope.launch {
            familyStorageAPI.renameServerObject(
                familyId = USER.family,
                request = MediaFileInfo(id = id, name = newName)
            )
        }
    }

    override fun downloadFile(
        file: File,
        onSuccess: (dFile: java.io.File, contentType: String) -> Unit,
        onError: () -> Unit
    ) {
        scope.launch {
            val dFile = java.io.File(
                AppApplication.app.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                file.name
            )
            try {
                mediaStorageService.getFileFromServer(
                    file = dFile,
                    fileInfo = MediaFileInfo(
                        id = file.id,
                        name = file.name,
                        familyId = USER.family,
                        type = EMediaType.STORAGE_FILE
                    )
                )
                onSuccess(dFile, "*/*")
            } catch (e: Exception) {
                e.printStackTrace()
                onError()
            }

        }
    }

    override suspend fun getStorageContent(node: String): Result<List<StorageObject>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getContent(USER.family, storageType = node)
//                val adapter = moshi.adapter(StorageObject::class.java)
//                val result = adapter.fromJson()
//                val adapter = StorageObjectAdapter()
//                val result = adapter.fromJson(response) as StorageFolder
                val result = response.toStorageObject() as StorageFolder
                if (node == NODE_IMAGE_STORAGE) {
                    prepareStorageFolder(result)
                }
                return@withContext Result.Success(result.value)
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext Result.Error(e)
            }
        }
    }

    private fun prepareStorageFolder(result: StorageObject) {
        if (result.isFile()) {
            (result as File).value = getImageUrlFromMediaFileInfo(
                MediaFileInfo(
                    id = result.id,
                    type = EMediaType.STORAGE_IMAGE,
                    familyId = USER.family,
                )
            )
        } else {
            (result as StorageFolder).value.forEach { prepareStorageFolder(it) }
        }
    }
}