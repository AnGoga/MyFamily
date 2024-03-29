package com.angogasapps.myfamily.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Editable
import androidx.annotation.StringRes
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.models.DairyObject
import com.angogasapps.myfamily.models.storage.ArrayFolder
import com.angogasapps.myfamily.models.storage.MapFolder
import com.angogasapps.myfamily.models.storage.StorageObject
import com.angogasapps.myfamily.network.spring_models.media_storage.MediaFileInfo
import com.google.firebase.database.DataSnapshot
import es.dmoral.toasty.Toasty
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


private const val dateFormat = "dd/MM/yyyy"

fun Long.asDate(): String = SimpleDateFormat(dateFormat).format(Date(this))
//        .split("/").toMutableList().also { it[1] = it[1] + 1 }.joinToString("/")

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun String.asMillis(): Long = SimpleDateFormat(dateFormat).parse(this).time

fun toDate(year: Int, month: Int, day: Int): String {
    return "$day/$month/$year"
}

fun ArrayList<DairyObject>.haveWithKey(dairy: DairyObject): Boolean {
    return this.indexOfThisKey(dairy) >= 0
}

fun ArrayList<DairyObject>.indexOfThisKey(dairy: DairyObject): Int {
    this.forEachIndexed { index, it ->
        run {
            if (it.key == dairy.key)
                return index
        }
    }
    return -1
}

fun DataSnapshot.asString(): String? = this.getValue(String::class.java)

fun List<StorageObject>.toMapFolder(): HashMap<String, StorageObject> {
    val map = HashMap<String, StorageObject>()

    for (obj: StorageObject in this) {
        if (obj.isFile()) {
            map[obj.id] = obj
        } else if (obj.isFolder()) {
            obj as ArrayFolder
            val map1 = obj.value.toMapFolder()
            val folder = MapFolder(obj.id, obj.name, map1)
            map[obj.id] = folder
        }
    }
    return map
}

fun downloadBitmapByURL(url: String): Bitmap? {
    return try {
        val photoUrl = URL(url)
        val downloadStream = photoUrl.openStream()
        BitmapFactory.decodeStream(downloadStream)
    } catch (e: Exception) {
        null
    }
}


fun Uri.getRealPathFromURI(): String? {
    val result: String?
    val cursor = appComponent.context.getContentResolver().query(this, null, null, null, null)
    if (cursor == null) {
        result = this.path
    } else {
        cursor.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        result = cursor.getString(idx)
        cursor.close()
    }
    return result
}

fun getPath(context: Context, uri: Uri): String? {
    if ("content".equals(uri.scheme, ignoreCase = true)) {
        val projection = arrayOf("_data")
        try {
            val cursor = context.getContentResolver().query(uri, projection, null, null, null)
            val column_index: Int = cursor?.getColumnIndexOrThrow("_data")!!
            if (cursor.moveToFirst()!!) {
                return cursor.getString(column_index)
            }
        } catch (e: java.lang.Exception) {
            // Eat it
        }
    } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
        return uri.path
    }
    return null
}

val FILE_SELECT_CODE = 129202

fun showFileChooser(context: Activity) {
    val intent = Intent(Intent.ACTION_GET_CONTENT).also {
        it.type = "*/*"
        it.addCategory(Intent.CATEGORY_OPENABLE)
    }
    try {
        context.startActivityForResult(
            Intent.createChooser(intent, context.getString(R.string.choose_file)),
            FILE_SELECT_CODE
        )
    } catch (ex: ActivityNotFoundException) {
        Toasty.error(context, context.getString(R.string.file_manager_not_found)).show()
    }
}


fun stringOf(@StringRes res: Int): String {
    return AppApplication.app.applicationContext.getString(res)
}

fun showInDevelopingToast() {
    Toasty.info(AppApplication.app, R.string.in_developing).show()
}


fun getImageUrlFromMediaFileInfo(info: MediaFileInfo): String {
    return appComponent.retrofit.baseUrl().toUrl()
        .toString() + "media_storage/media/storage/get/image/families/${info.familyId}/types/${info.type}/ids/${info.id}".also { println(it)  }

}
