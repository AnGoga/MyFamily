package com.angogasapps.myfamily.ui.screens.family_storage.storage_adapters

import android.content.Context
import android.content.Intent
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.models.storage.File
import com.angogasapps.myfamily.ui.screens.family_storage.StorageNoteBuilderActivity

class NoteStorageAdapter(context: Context, rootNode: String, onChangeDirectory: (dirName: String) -> Unit)
    : BaseStorageAdapter(context, rootNode, onChangeDirectory) {
    override fun onFileClick(file: File) {
        context.startActivity(Intent(context, StorageNoteBuilderActivity::class.java)
                .also {
                    it.putExtra(CHILD_NAME, file.name)
                    it.putExtra(CHILD_ID, file.id)
                    it.putExtra(CHILD_VALUE, file.value)
                    it.putExtra(ROOT_FOLDER, getRootFolderId())
                }
        )
    }
}