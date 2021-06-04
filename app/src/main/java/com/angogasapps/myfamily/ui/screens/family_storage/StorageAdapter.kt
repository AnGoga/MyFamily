package com.angogasapps.myfamily.ui.screens.family_storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.databinding.StorageViewHolderBinding
import com.angogasapps.myfamily.models.storage.StorageObject

class StorageAdapter: RecyclerView.Adapter<StorageAdapter.StorageHolder> {
    companion object{
        val fileDraw = AppApplication.getInstance().resources.getDrawable(R.drawable.ic_file)
        val folderDraw = AppApplication.getInstance().resources.getDrawable(R.drawable.ic_folder)
    }

    var list: ArrayList<StorageObject> = ArrayList()
    val context: Context

    constructor(context: Context){
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorageHolder {
        return StorageHolder(View.inflate(context, R.layout.storage_view_holder, parent))
    }

    override fun onBindViewHolder(holder: StorageHolder, position: Int) {
        holder.update(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class StorageHolder : RecyclerView.ViewHolder {

        val binding: StorageViewHolderBinding
        constructor(itemView: View) : super(itemView){
            binding = StorageViewHolderBinding.bind(itemView)
        }

        fun update(obj: StorageObject) {
            if (obj.isFile()){
                binding.image.setImageDrawable(fileDraw)
            }else if (obj.isFolder()){
                binding.image.setImageDrawable(folderDraw)
            }
        }
    }


}