package com.angogasapps.myfamily.utils.moshi.adapters

import com.angogasapps.myfamily.models.storage.*
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson


//TODO!!!!!!!!
class StorageObjectAdapter {
    @FromJson
    fun fromJson(obj: JsonStorageObject): StorageObject = when (obj.file) {
        true -> File(id = obj.id, name = obj.name, value = obj.value)
        false -> StorageFolder(id = obj.id, name = obj.name, value = obj.values as MutableList<StorageObject>)
    }

    @ToJson
    fun toJson(obj: StorageObject): JsonStorageObject = when (obj.isFile()) {
        true -> JsonStorageObject(id = obj.id, name = obj.name, value = (obj as File).value, type = obj.type)
        false -> JsonStorageObject(id = obj.id, name = obj.name, values = (obj as StorageFolder).value as MutableList<JsonStorageObject>, type = obj.type)
    }
}