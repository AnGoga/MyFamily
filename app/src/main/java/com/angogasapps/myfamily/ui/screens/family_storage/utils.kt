package com.angogasapps.myfamily.ui.screens.family_storage

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.ImageView
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.firebase.NODE_IMAGE_STORAGE
import com.angogasapps.myfamily.models.storage.ArrayFolder
import com.angogasapps.myfamily.models.storage.File
import com.angogasapps.myfamily.network.interfaces.family_stoarge.FamilyStorageService
import com.angogasapps.myfamily.ui.screens.family_storage.dialogs.NameGetterDialog
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun showRemoveFileDialog(
    context: Context,
    file: File,
    folderId: String,
    rootNode: String,
    storageService: FamilyStorageService
) {
    val listener: (dialog: DialogInterface, which: Int) -> Unit = { dialog, which ->
        if (which == AlertDialog.BUTTON_POSITIVE) {
            storageService.removeFile(file = file, folderId = folderId, rootNode = rootNode,
                onError = {
                    Toasty.error(context, R.string.something_went_wrong).show()
                },
                onSuccess = {

                }
            )

        } else if (which == AlertDialog.BUTTON_NEGATIVE) {
        }

        dialog.dismiss()
    }

    val builder = AlertDialog.Builder(context)
    builder.setTitle(context.getString(R.string.remove_file))
    builder.setMessage(
        context.getString(R.string.dialog_delet_file_accept)
            .replace("file_name", "\"${file.name}\"")
    )
    builder.setNegativeButton(R.string.rescue, listener)
    builder.setPositiveButton(R.string.remove, listener)
    builder.create().show()
}

fun showAcceptRemoveImageDialog(
    context: Context,
    image: ImageView,
    imageFile: File,
    folderId: String,
    storageService: FamilyStorageService,
    onSuccessRemove: () -> Unit
) {
    val listener: (dialog: DialogInterface, which: Int) -> Unit = { dialog, which ->
        if (which == AlertDialog.BUTTON_POSITIVE) {
            storageService.removeFile(
                file = imageFile,
                folderId = folderId,
                rootNode = NODE_IMAGE_STORAGE,
                onSuccess = onSuccessRemove
            )
        } else if (which == AlertDialog.BUTTON_NEGATIVE) {
        }

        dialog.cancel()
    }

    val builder = AlertDialog.Builder(context)
    builder.setTitle(context.getString(R.string.remove_image))
    builder.setMessage(context.getString(R.string.accept_remove_image))
    builder.setNegativeButton(R.string.rescue, listener)
    builder.setPositiveButton(R.string.remove, listener)
    val dialog = builder.create()
    dialog.setIcon(image.drawable)
//    dialog.setView(image)
    dialog.show()
}

fun showOnLongClickFolderDialog(
    context: Context,
    folder: ArrayFolder,
    rootNode: String,
    rootFolderId: String,
    storageService: FamilyStorageService
) {
    val builder = AlertDialog.Builder(context)
    val array = arrayOf(context.getString(R.string.rename), context.getString(R.string.remove))
    builder.setItems(array) { dialog, which ->
        when (which) {
            0 -> {
                showRenameFolderDialog(context, folder, rootNode, storageService)
            }
            1 -> {
                showRemoveFolderDialog(context, folder, rootNode, rootFolderId, storageService)
            }
        }
        dialog.dismiss()
    }
    builder.create().show()
}

fun showRenameFolderDialog(
    context: Context,
    folder: ArrayFolder,
    rootNode: String,
    storageService: FamilyStorageService
) {
    NameGetterDialog(context).show(isFolder = true, name = folder.name) { name ->
        storageService.renameFolder(folder = folder, newName = name, rootNode = rootNode)
    }
}

fun showRemoveFolderDialog(
    context: Context,
    folder: ArrayFolder,
    rootNode: String,
    rootFolderId: String,
    storageService: FamilyStorageService
) {
    val listener: (dialog: DialogInterface, which: Int) -> Unit = { dialog, which ->
        if (which == AlertDialog.BUTTON_POSITIVE) {
            GlobalScope.launch(Dispatchers.Default) {
                storageService.removeFolder(folder, rootNode, rootFolderId)
            }
        } else if (which == AlertDialog.BUTTON_NEGATIVE) {
        }
        dialog.dismiss()
    }

    AlertDialog.Builder(context)
        .setTitle(context.getString(R.string.remove_folder))
        .setMessage(
            context.getString(R.string.accept_remove_folder)
                .replace("folder_name", "\"${folder.name}\"")
        )
        .setPositiveButton(R.string.remove, listener)
        .setNegativeButton(R.string.cancel, listener)
        .create().show()
}


fun showOnLongClickFileDialog(
    context: Context,
    file: File,
    rootNode: String,
    rootFolderId: String,
    storageService: FamilyStorageService
) {
    val builder = AlertDialog.Builder(context)
    val array = arrayOf(context.getString(R.string.rename), context.getString(R.string.remove))
    builder.setItems(array) { dialog, which ->
        when (which) {
            0 -> {
                showRenameFileDialog(context, file, rootNode, rootFolderId, storageService)
            }
            1 -> {
                showRemoveFileDialog(
                    context = context,
                    folderId = rootFolderId,
                    file = file,
                    rootNode = rootNode,
                    storageService = storageService
                )
            }
        }
        dialog.dismiss()
    }
    builder.create().show()
}

fun showRenameFileDialog(
    context: Context,
    file: File,
    rootNode: String,
    folderId: String,
    storageService: FamilyStorageService
) {
    NameGetterDialog(context).show(isFolder = false, name = file.name) {
        storageService.renameFile(
            file = file,
            newName = it,
            rootNode = rootNode,
            folderId = folderId,
            onSuccess = {
                file.name = it
            })
    }
}


