package com.angogasapps.myfamily.models.storage

import android.os.Parcelable
import com.angogasapps.myfamily.firebase.CHILD_TYPE
import com.google.firebase.database.DataSnapshot
import kotlinx.android.parcel.Parcelize


const val TYPE_FOLDER = "folder"
const val TYPE_FILE = "file"

abstract class StorageObject(open var id: String, open var name: String) {
    abstract val type: String

    abstract fun isFile(): Boolean
    abstract fun isFolder(): Boolean
}

data class File(override var id: String, override var name: String, var value: String): StorageObject(id, name){
    override val type: String
        get() = TYPE_FILE



    override fun isFile(): Boolean = true
    override fun isFolder(): Boolean = false
}

abstract class Folder(override var id: String, override var name: String) : StorageObject(id, name){
    override val type: String
        get() = TYPE_FOLDER
    override fun isFile(): Boolean = false
    override fun isFolder(): Boolean = true
}
data class MapFolder(override var id: String, override var name: String, var files: HashMap<String, StorageObject>): Folder(id, name)
data class ArrayFolder(override var id: String, override var name: String, var value: ArrayList<StorageObject>): Folder(id, name)



