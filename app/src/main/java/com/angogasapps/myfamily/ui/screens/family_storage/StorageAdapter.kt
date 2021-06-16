package com.angogasapps.myfamily.ui.screens.family_storage

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.databinding.StorageViewHolderBinding
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*
import com.angogasapps.myfamily.models.storage.ArrayFolder
import com.angogasapps.myfamily.models.storage.File
import com.angogasapps.myfamily.models.storage.StorageObject
import com.angogasapps.myfamily.ui.screens.family_storage.gallery_activity.ImageGalleryActivity
import java.util.*
import kotlin.collections.ArrayList

class StorageAdapter(val context: Context, val rootNode: String, var onChangeDirectory: (dirName: String) -> Unit)
        : RecyclerView.Adapter<StorageAdapter.StorageHolder>() {
    companion object{
        val fileDraw = AppApplication.getInstance().resources.getDrawable(R.drawable.ic_file)
        val folderDraw = AppApplication.getInstance().resources.getDrawable(R.drawable.ic_folder)
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
        namesStack.push(AppApplication.getInstance().getString(R.string.app_name))
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
                binding.root.setOnClickListener {
                    if (rootNode == NODE_NOTE_STORAGE)
                        context.startActivity(Intent(context, StorageNoteBuilderActivity::class.java)
                            .also {
                                it.putExtra(CHILD_NAME, file.name)
                                it.putExtra(CHILD_ID, file.id)
                                it.putExtra(CHILD_VALUE, file.value)
                                it.putExtra(ROOT_FOLDER, getRootFolderId())
                            }
                        )
                }
                binding.root.setOnLongClickListener {
//                    showRemoveFileDialog(context = context, folderId = getRootFolderId(), file = file, rootNode = rootNode)
                    showOnLongClickFileDialog(context = context, file = file, rootNode = rootNode, rootFolderId = getRootFolderId())
                    return@setOnLongClickListener true
                }
            }else if (obj.isFolder()){
                binding.image.setImageDrawable(folderDraw)
                binding.root.setOnClickListener {
                    if (rootNode == NODE_IMAGE_STORAGE){
                        context.startActivity(Intent(context, ImageGalleryActivity::class.java)
                                .also { it.putExtra(CHILD_ID, obj.id) })
                    }else {
                        showFolder(obj.id)
                    }
                }
                binding.root.setOnLongClickListener {
                    showOnLongClickFolderDialog(context, obj as ArrayFolder, rootNode, getRootFolderId())
                    return@setOnLongClickListener true
                }
            }
        }
    }
}