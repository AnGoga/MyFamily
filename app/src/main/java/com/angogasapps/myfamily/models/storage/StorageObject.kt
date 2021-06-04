package com.angogasapps.myfamily.models.storage

import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_TYPE
import com.google.firebase.database.DataSnapshot

/*




abstract class StorageObject(snapshot: DataSnapshot){
    abstract val type: String

    val id: String
    abstract val value: Any

    init {
        id = snapshot.key!!
    }
}

class Folder(snapshot: DataSnapshot): StorageObject(snapshot) {
    override val type: String
        get() = TYPE_FOLDER

    override val value: ArrayList<String>?

    init {
        value =
    }
}
/*

    val id: String
    val value: String

    init {
        id = snapshot.key.toString()
        value = snapshot.
    }
 */

class File(snapshot: DataSnapshot) : StorageObject(snapshot) {
    override val type: String
        get() = TYPE_FILE

    override val value: String

    init {
        value = snapshot.child(CHILD_TYPE).getValue(String::class.java)!!
    }

}
*/

const val TYPE_FOLDER = "folder"
const val TYPE_FILE = "file"

abstract class StorageObject(open var id: String, open var name: String){
    abstract val type: String

    abstract fun isFile(): Boolean
    abstract fun isFolder(): Boolean
}

data class Folder(override var id: String, override var name: String, var value: ArrayList<StorageObject>): StorageObject(id, name) {
    override val type: String
        get() = TYPE_FOLDER

    override fun isFile(): Boolean = false
    override fun isFolder(): Boolean = true
}

data class File(override var id: String, override var name: String, var value: String): StorageObject(id, name){
    override val type: String
        get() = TYPE_FILE

    override fun isFile(): Boolean = true
    override fun isFolder(): Boolean = false
}