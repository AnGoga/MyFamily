package com.angogasapps.myfamily.objects

import android.app.Activity
import kotlin.jvm.Volatile
import android.media.MediaRecorder
import java.io.File
import java.io.IOException
import java.lang.Exception
import kotlin.Throws

class ChatAudioRecorder(
    private val activity: Activity,
    val key: String
) {
    @Volatile
    var file: File? = null
        private set

    private val recorder: MediaRecorder = MediaRecorder()
    fun startRecording() {
        object : Thread() {
            override fun run() {
                super.run()
                try {
                    file = createNewFile()
                    prepareRecorder()
                    recorder.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    fun stopRecording(onSuccess: () -> Unit) {
        try {
            recorder.stop()
            onSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun prepareRecorder() {
        recorder.reset()
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
        recorder.setOutputFile(file!!.absolutePath)
        recorder.prepare()
    }

    private fun createNewFile(): File {
        val file = File(activity.filesDir, key)
        try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

}