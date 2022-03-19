package com.angogasapps.myfamily.models.storage

import com.squareup.moshi.JsonClass


const val TYPE_FOLDER = "folder"
const val TYPE_FILE = "file"

abstract class StorageObject(open var id: String, open var name: String) {
    abstract val type: String

    abstract fun isFile(): Boolean
    abstract fun isFolder(): Boolean
}

@JsonClass(generateAdapter = true)
data class File(override var id: String, override var name: String, var value: String) :
    StorageObject(id, name) {
    override val type: String
        get() = TYPE_FILE


    override fun isFile(): Boolean = true
    override fun isFolder(): Boolean = false
}

abstract class Folder(override var id: String, override var name: String) :
    StorageObject(id, name) {
    override val type: String
        get() = TYPE_FOLDER

    override fun isFile(): Boolean = false
    override fun isFolder(): Boolean = true
}

data class MapFolder(
    override var id: String,
    override var name: String,
    var files: HashMap<String, StorageObject>
) : Folder(id, name)

@JsonClass(generateAdapter = true)
class ArrayFolder(
    override var id: String,
    override var name: String,
    var value: MutableList<StorageObject>
) : Folder(id, name) {
    companion object {

    }
//    var value = value
//        get() = field as ArrayList

}

fun List<JsonStorageObject>.toStorageObjectsList() : MutableList<StorageObject> {
    return this.map { it.toStorageObject() }.toMutableList()
}


typealias StorageFolder = ArrayFolder

@JsonClass(generateAdapter = true)
class JsonStorageObject(
    override var id: String = "",
    override var name: String = "",
    override var type: String = "",
    var value: String = "",
    var values: MutableList<JsonStorageObject> = mutableListOf(),
    var file: Boolean = false
): StorageObject(id, name)  {

    fun toStorageObject(): StorageObject {
        return if (file)
            File(id = id, name = name, value = value)
        else
            StorageFolder(id = id, name = name, value = values.toStorageObjectsList())
    }

    override fun isFile() = file
    override fun isFolder() = !file

}


