package com.angogasapps.myfamily.ui.screens.personal_dairy

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.OnScanCompletedListener
import android.net.Uri
import android.os.Environment
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.database.DatabaseManager
import com.angogasapps.myfamily.models.DairyObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID.randomUUID
import java.util.concurrent.CountDownLatch


open class DairyDatabaseManager private constructor(){
    companion object{
        private var manager: DairyDatabaseManager? = null

        fun getInstance(): DairyDatabaseManager{
            synchronized(DairyDatabaseManager::class.java) {
                if (manager == null)
                    manager = DairyDatabaseManager()
                return manager!!
            }
        }
    }

    suspend fun saveDairy(dairy: DairyObject){
        coroutineScope {
            launch(Dispatchers.IO) {
                println(dairy.toString())


                dairy.uri = saveImageToAppStorage(dairy.uri!!)
                DatabaseManager.getInstance().dairyDao.insert(dairy)
            }
        }
    }

    private fun saveImageToAppStorage(uri: String): String {
        return saveBitmap(BitmapFactory.decodeFile(Uri.parse(uri).path)).toString()
    }

    private fun saveBitmap(bitmap: Bitmap): Uri {
        val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
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


//        var fileUri: Uri? = null
//        val lock = CountDownLatch(1)
//        val listener = OnScanCompletedListener { path, uri ->
//            run {
//                fileUri = uri
//                lock.countDown()
//                println(uri)
//            }
//        }
//        MediaScannerConnection.scanFile(
//                AppApplication.getInstance().applicationContext,
//                arrayOf(file.absolutePath),
//                null,
//                listener);
//        lock.await()
//        return Uri.fromFile(file)


        val uri = file.toURI()
        print(uri)
        return Uri.parse(uri.toString())

    }

}