package com.angogasapps.myfamily.ui.screens.family_storage.gallery_storage_adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.firebase.NODE_IMAGE_STORAGE
import com.angogasapps.myfamily.firebase.NODE_VIDEO_STORAGE
import com.angogasapps.myfamily.models.storage.ArrayFolder
import com.angogasapps.myfamily.models.storage.File
import com.angogasapps.myfamily.network.interfaces.family_stoarge.FamilyStorageService
import com.angogasapps.myfamily.objects.ChatImageShower
import com.angogasapps.myfamily.ui.screens.family_storage.StorageManager
import com.angogasapps.myfamily.ui.screens.family_storage.showAcceptRemoveImageDialog
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope


class MediaGalleryStorageAdapter(
    val context: Activity,
    val scope: CoroutineScope,
    var folder: ArrayFolder,
    val rootNode: String,
    val storageService: FamilyStorageService
) : RecyclerView.Adapter<MediaGalleryStorageAdapter.BaseStorageGalleryHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseStorageGalleryHolder {
        return when (viewType) {
            0 -> ImageGalleryHolder(inflater.inflate(R.layout.image_gallery_holder, parent, false))
            1 -> VideoGalleryHolder(inflater.inflate(R.layout.video_gallery_holder, parent, false))
            else -> ImageGalleryHolder(
                inflater.inflate(
                    R.layout.image_gallery_holder,
                    parent,
                    false
                )
            )

        }
    }

    override fun onBindViewHolder(holder: BaseStorageGalleryHolder, position: Int) {
        holder.update((folder.value[position] as File).value)
    }

    override fun getItemCount(): Int = folder.value.size

    override fun getItemViewType(position: Int): Int {
        return when (rootNode) {
            NODE_IMAGE_STORAGE -> 0
            NODE_VIDEO_STORAGE -> 1
            else -> -1
        }
    }

    fun add(url: String, key: String) {
        folder.value.add(File(value = url, id = key, name = ""))
        notifyItemInserted(itemCount - 1)
    }

    fun update() {
        for (obj in StorageManager.getInstance().list) {
            if (obj.id == folder.id) {
                folder = obj as ArrayFolder
                notifyDataSetChanged()
                return
            }
        }
        context.finish()
    }

    abstract inner class BaseStorageGalleryHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        abstract fun update(value: String)
    }


    inner class ImageGalleryHolder(itemView: View) : BaseStorageGalleryHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        override fun update(value: String) {
            Glide.with(context).load(value).into(image)
            this.itemView.setOnClickListener {
                ChatImageShower(context as AppCompatActivity).showImage(image)
            }
            this.itemView.setOnLongClickListener {
                val imageFile = folder.value[position] as File
                showAcceptRemoveImageDialog(
                    context = context,
                    image = image,
                    imageFile = imageFile,
                    folderId = folder.id,
                    storageService = storageService,
                    onSuccessRemove = {
                        var ind = 0
                        folder.value.forEachIndexed { index, obj ->
                            if (obj.id == imageFile.id) {
                                ind = index
                                return@forEachIndexed
                            }
                        }
                        folder.value.removeAt(ind)
                        notifyItemRemoved(ind)
                    }
                )
                return@setOnLongClickListener true
            }
        }
    }

    inner class VideoGalleryHolder(itemView: View) : BaseStorageGalleryHolder(itemView) {
        override fun update(value: String) {

        }
    }
}