package com.angogasapps.myfamily.objects

import android.content.Context
import android.media.MediaPlayer
import com.angogasapps.myfamily.objects.ChatVoicePlayer.IOnEndPlay
import com.angogasapps.myfamily.firebase.ChatFunks
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import android.media.MediaPlayer.OnCompletionListener
import es.dmoral.toasty.Toasty
import com.angogasapps.myfamily.R
import java.io.File
import java.lang.Exception

class ChatVoicePlayer(private val context: Context) {
    private lateinit var mMediaPlayer: MediaPlayer
    private lateinit var voiceFile: File

    fun play(messageKey: String, fileUrl: String, iOnEndPlay: IOnEndPlay) {
        voiceFile = File(context.filesDir.absolutePath, messageKey)
        if (voiceFile.exists() && voiceFile.isFile && voiceFile.length() > 0) {
            startPlayer(iOnEndPlay)
        } else {
            try {
                voiceFile.createNewFile()
                ChatFunks.getVoiceFileFromStorage(
                    voiceFile,
                    messageKey,
                    object : IOnEndCommunicationWithFirebase {
                        override fun onSuccess() {
                            startPlayer(iOnEndPlay)
                        }

                        override fun onFailure() {}
                    })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun startPlayer(iOnEndPlay: IOnEndPlay) {
        try {
            mMediaPlayer.setDataSource(voiceFile.absolutePath)
            mMediaPlayer.prepare()
            mMediaPlayer.start()
            mMediaPlayer.setOnCompletionListener { mp: MediaPlayer? -> stop(iOnEndPlay) }
        } catch (e: Exception) {
            e.printStackTrace()
            Toasty.error(context, R.string.something_went_wrong).show()
        }
    }

    fun stop(iOnEndPlay: IOnEndPlay) {
        try {
            mMediaPlayer.stop()
            mMediaPlayer.reset()
        } catch (e: Exception) {
            e.printStackTrace()
            Toasty.error(context, R.string.something_went_wrong).show()
        }
        iOnEndPlay.onEndPlay()
    }

    fun release() {
        mMediaPlayer.release()
    }

    fun init() {
        mMediaPlayer = MediaPlayer()
    }

    interface IOnEndPlay {
        fun onEndPlay()
    }
}