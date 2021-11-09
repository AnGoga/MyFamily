package com.angogasapps.myfamily.ui.screens.family_storage.dialogs

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.firebase.NODE_IMAGE_STORAGE
import com.angogasapps.myfamily.models.storage.File
import com.angogasapps.myfamily.network.interfaces.family_stoarge.FamilyStorageService
import com.angogasapps.myfamily.objects.ChatImageShower
import com.angogasapps.myfamily.objects.ChatImageShower.ImageShowerDialog


class GalleryImageShower(val context: AppCompatActivity, storageService: FamilyStorageService) {
    private val dialog: GalleryImageShowerDialog = GalleryImageShowerDialog(storageService)

    fun showImage(imageView: ImageView, file: File, folderId: String) {
        dialog.init(imageView, file, folderId)
        dialog.show(context.supportFragmentManager.beginTransaction(), ImageShowerDialog.TAG)
    }

    companion object {
        class GalleryImageShowerDialog(val storageService: FamilyStorageService) :
            ChatImageShower.ImageShowerDialog() {
            var file: File? = null
            var folderId: String? = null

            fun init(imageView: ImageView, file: File, folderId: String) {
                this.setImage(imageView)
                this.file = file
                this.folderId = folderId
            }

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setStyle(STYLE_NORMAL, android.R.style.ThemeOverlay_Material_Dark_ActionBar)
            }

            override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
                super.onCreateOptionsMenu(menu, inflater)
                inflater.inflate(R.menu.gallery_menu, menu)
            }

            override fun onOptionsItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.remove -> {
                        try {
                            storageService.removeFile(
                                file = file!!,
                                folderId = folderId!!,
                                rootNode = NODE_IMAGE_STORAGE
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        dismiss()
                    }
                }
                return super.onOptionsItemSelected(item)
            }
        }
    }
}