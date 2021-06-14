package com.angogasapps.myfamily.ui.screens.family_storage.gallery_activity

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.models.storage.ArrayFolder
import com.angogasapps.myfamily.models.storage.File
import com.bumptech.glide.Glide

class ImageGalleryAdapter(val context: Context, val folder: ArrayFolder): RecyclerView.Adapter<ImageGalleryAdapter.ImageGalleryHolder>() {
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



    inner class ImageGalleryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)


        fun update(value: String){
            Glide.with(context).load(value).into(image)
        }
    }

}