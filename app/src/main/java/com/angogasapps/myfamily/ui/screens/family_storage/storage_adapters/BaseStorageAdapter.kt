package com.angogasapps.myfamily.ui.screens.family_storage.storage_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.databinding.StorageViewHolderBinding
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.models.storage.ArrayFolder
import com.angogasapps.myfamily.models.storage.File
import com.angogasapps.myfamily.models.storage.StorageObject
import com.angogasapps.myfamily.ui.screens.family_storage.StorageManager
import com.angogasapps.myfamily.ui.screens.family_storage.showOnLongClickFileDialog
import com.angogasapps.myfamily.ui.screens.family_storage.showOnLongClickFolderDialog
import java.util.*
import kotlin.collections.ArrayList

open class BaseStorageAdapter(val context: Context, val rootNode: String, var onChangeDirectory: (dirName: String) -> Unit)
        : RecyclerView.Adapter<BaseStorageAdapter.StorageHolder>() {
    companion object{
        val fileDraw = AppApplication.app.resources.getDrawable(R.drawable.ic_file)
        val folderDraw = AppApplication.app.resources.getDrawable(R.drawable.ic_folder)
    }

    var stack: Stack<String> = Stack()
    val namesStack: Stack<String> = Stack()
    var list: ArrayList<StorageObject> = ArrayList()
    val inflater: LayoutInflater = LayoutInflater.from(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorageHolder {
        return StorageHolder(inflater.inflate(R.layout.storage_view_holder, parent, false))
    }

    override fun onBindViewHolder(holder: StorageHolder, position: Int) {
        holder.update(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun getRootFolderId(): String {
        return if (stack.empty()) CHILD_BASE_FOLDER else stack.peek()
    }


    fun update() {
        this.list = StorageManager.getInstance().list
        namesStack.clear()
        namesStack.push(AppApplication.app.getString(R.string.app_name))
        val cashStack = Stack<String>()

        for (id in stack){
            for (obj in list){
                if (obj.id == id){
                    this.list = (obj as ArrayFolder).value
                    cashStack.push(id)
                    namesStack.push(obj.name)
                    onChangeDirectory(obj.name)
                }
            }
        }
        this.stack = cashStack
        notifyDataSetChanged()
    }

    fun showFolder(id: String){
        stack.push(id)
        for (obj in list){
            if (obj.id == id) {
                this.list = (obj as ArrayFolder).value
                namesStack.push(obj.name)
                onChangeDirectory(obj.name)
                break
            }
        }

        notifyDataSetChanged()
    }

    fun exitFromUpFolder(): Boolean {
        if (stack.isEmpty()) return false
        stack.pop()
        this.list = StorageManager.getInstance().getListByStack(stack)
        namesStack.pop()
        onChangeDirectory(namesStack.peek())
        notifyDataSetChanged()
        return true
    }

    protected open fun onFileClick(file: File){}
    protected open fun onFolderClick(folder: ArrayFolder){showFolder(folder.id)}

    inner class StorageHolder : RecyclerView.ViewHolder {
        val binding: StorageViewHolderBinding

        constructor(itemView: View) : super(itemView) {
            binding = StorageViewHolderBinding.bind(itemView)
        }

        fun update(obj: StorageObject) {
            binding.text.text = obj.name
            binding.root.setOnClickListener(null)
            if (obj.isFile()){
                val file = obj as File
                binding.image.setImageDrawable(fileDraw)
                binding.root.setOnClickListener { onFileClick(file) }
                binding.root.setOnLongClickListener {
                    showOnLongClickFileDialog(context = context, file = file, rootNode = rootNode, rootFolderId = getRootFolderId())
                    return@setOnLongClickListener true
                }
            }else if (obj.isFolder()){
                binding.image.setImageDrawable(folderDraw)
                val folder = obj as ArrayFolder

                binding.root.setOnClickListener {
                    onFolderClick(folder)
                }
                binding.root.setOnLongClickListener {
                    showOnLongClickFolderDialog(context, folder, rootNode, getRootFolderId())
                    return@setOnLongClickListener true
                }
            }
        }
    }
}