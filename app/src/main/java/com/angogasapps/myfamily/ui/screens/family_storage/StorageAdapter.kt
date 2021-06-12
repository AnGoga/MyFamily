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
import com.angogasapps.myfamily.models.storage.ArrayFolder
import com.angogasapps.myfamily.models.storage.StorageObject
import java.util.*
import kotlin.collections.ArrayList

class StorageAdapter(val context: Context, var onChangeDirectory: (dirName: String) -> Unit)
        : RecyclerView.Adapter<StorageAdapter.StorageHolder>() {
    companion object{
        val fileDraw = AppApplication.getInstance().resources.getDrawable(R.drawable.ic_file)
        val folderDraw = AppApplication.getInstance().resources.getDrawable(R.drawable.ic_folder)
    }

    val stack: Stack<String> = Stack()
    var list: ArrayList<StorageObject> = ArrayList()
    val inflater: LayoutInflater = LayoutInflater.from(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorageHolder {
        return StorageHolder(inflater.inflate(R.layout.storage_view_holder, parent, false))
    }

    override fun onBindViewHolder(holder: StorageHolder, position: Int) {
        holder.update(list[position])
    }

    override fun getItemCount(): Int = list.size


    fun update() {
        this.list = StorageManager.getInstance().list
        notifyDataSetChanged()
    }

/*    fun showFolder(id: String){
        stack.push(id)
        this.list = StorageManager.getInstance().getListByStack(stack)
        notifyDataSetChanged()
    }    */

    fun showFolder(id: String){
        stack.push(id)
        for (obj in list){
            if (obj.id == id) {
                this.list = (obj as ArrayFolder).value
                onChangeDirectory(obj.name)
                break
            }
        }

        notifyDataSetChanged()
    }

    fun exitFromUpFolder(): Boolean{
        if (stack.isEmpty()) return false
        stack.pop()
        this.list = StorageManager.getInstance().getListByStack(stack)
        notifyDataSetChanged()

        return true
    }

    inner class StorageHolder : RecyclerView.ViewHolder {
        val binding: StorageViewHolderBinding

        constructor(itemView: View) : super(itemView) {
            binding = StorageViewHolderBinding.bind(itemView)
        }

        fun update(obj: StorageObject) {
            binding.text.text = obj.name
            binding.root.setOnClickListener(null)
            if (obj.isFile()){
                binding.image.setImageDrawable(fileDraw)
            }else if (obj.isFolder()){
                binding.image.setImageDrawable(folderDraw)
                binding.root.setOnClickListener {
                    showFolder(obj.id)
                }
            }
        }

    }

}