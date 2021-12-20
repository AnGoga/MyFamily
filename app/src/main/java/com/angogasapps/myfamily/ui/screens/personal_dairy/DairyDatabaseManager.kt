package com.angogasapps.myfamily.ui.screens.personal_dairy

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.angogasapps.myfamily.database.DairyDao
import com.angogasapps.myfamily.models.DairyObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID.randomUUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DairyDatabaseManager @Inject constructor(
    private val dairyDao: DairyDao,
    private val dairyViewModel: PersonalDairyViewModel
) {


    suspend fun saveDairy(dairy: DairyObject) = withContext(Dispatchers.IO) {
        println(dairy.toString())
        if (dairy.uri != "null") {
            dairy.uri = saveImageToAppStorage(dairy.uri)
        }
        dairyDao.insert(dairy)
        dairyViewModel.addDairy(dairy)
    }

    private fun saveImageToAppStorage(uri: String): String {
        return saveBitmap(BitmapFactory.decodeFile(Uri.parse(uri).path)).toString()
    }

    private fun saveBitmap(bitmap: Bitmap): Uri {
        val root =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
        val myDir = File("$root/dairy_images")
        myDir.mkdirs()


        val file = File(myDir, randomUUID().toString())
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }


        val uri = file.toURI()
        print(uri)
        return Uri.parse(uri.toString())
    }

    suspend fun removeDairy(dairy: DairyObject) = withContext(Dispatchers.IO) {
        if (dairy.uri != "null")
            removeImage(dairy)
        dairyDao.delete(dairy)
        dairyViewModel.removeDairy(dairy)
    }

    private fun removeImage(dairy: DairyObject) {
        val root =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
        val myDir = File("$root/dairy_images")
        val file = File(myDir, dairy.uri.split("/").last())
        file.delete()
    }

    fun onDeleteApp() {
        val root =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
        val myDir = File("$root/dairy_images")

        if (myDir.exists() && myDir.isDirectory) {
            for (file: File in myDir.listFiles()) {
                file.delete()
            }
        }
    }

}