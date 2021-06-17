package com.angogasapps.myfamily.ui.screens.family_storage.gallery_activity

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.models.storage.ArrayFolder
import com.angogasapps.myfamily.models.storage.File
import com.angogasapps.myfamily.objects.ChatImageShower
import com.angogasapps.myfamily.ui.screens.family_storage.StorageManager
import com.angogasapps.myfamily.ui.screens.family_storage.showAcceptRemoveImageDialog
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope


class ImageGalleryAdapter(val context: Activity, val scope: CoroutineScope, var folder: ArrayFolder): RecyclerView.Adapter<ImageGalleryAdapter.ImageGalleryHolder>() {
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageGalleryHolder {
        return ImageGalleryHolder(inflater.inflate(R.layout.image_gallery_holder, parent, false))
    }

    override fun onBindViewHolder(holder: ImageGalleryHolder, position: Int) {
        holder.update((folder.value[position] as File).value)
    }

    override fun getItemCount(): Int = folder.value.size

    fun add(url: String, key: String){
        folder.value.add(File(value = url, id = key, name = ""))
        notifyItemInserted(itemCount - 1)
    }

    fun update(){
        for (obj in StorageManager.getInstance().list) {
            if (obj.id == folder.id) {
                folder = obj as ArrayFolder
                notifyDataSetChanged()
                return
            }
        }
        context.finish()
    }


    inner class ImageGalleryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)


        fun update(value: String){
            Glide.with(context).load(value).into(image)
//            scope.launch(Dispatchers.IO) {
//                val bm = downloadBitmapByURL(value)
//                withContext(Dispatchers.Main){ image.setImageBitmap(bm) }
//            }
            this.itemView.setOnClickListener {
                ChatImageShower(context as AppCompatActivity).showImage(image)
//                GalleryImageShower(context as AppCompatActivity)
//                        .showImage(imageView = image, folderId = folder.id, file = folder.value[position] as File)
            }
            this.itemView.setOnLongClickListener {
                val imageFile = folder.value[position] as File
                showAcceptRemoveImageDialog(
                        context = context, image = image,
                        imageFile = imageFile, folderId = folder.id,
                        onSuccessRemove = {
                            var ind = 0
                            folder.value.forEachIndexed {index, obj ->
                                if (obj.id == imageFile.id){
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

}