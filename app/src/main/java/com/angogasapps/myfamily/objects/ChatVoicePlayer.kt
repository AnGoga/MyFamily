package com.angogasapps.myfamily.objects

import android.content.Context
import android.media.MediaDataSource
import android.media.MediaPlayer
import android.media.MediaRecorder
import es.dmoral.toasty.Toasty
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.network.interfaces.chat.ChatVoiceGetter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception
import javax.inject.Inject

class ChatVoicePlayer(private val context: Context) {
    private lateinit var mMediaPlayer: MediaPlayer
    private lateinit var voiceFile: File
    @Inject
    lateinit var chatVoiceGetter: ChatVoiceGetter

    init {
        appComponent.inject(this)
    }


    fun play(messageKey: String, fileUrl: String, onEndPlay: () -> Unit) {
        voiceFile = File(context.filesDir.absolutePath, messageKey)
        if (voiceFile.exists() && voiceFile.isFile && voiceFile.length() > 0) {
            startPlayer(onEndPlay)
        } else {
            try {
                voiceFile.createNewFile()
                chatVoiceGetter.getVoiceFileFromStorage(
                    voiceFile,
                    messageKey,
                    onSuccess = {
                        startPlayer(onEndPlay)
                    }
                )


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun startPlayer(onEndPlay: () -> Unit) {
        try {
            mMediaPlayer.setDataSource(voiceFile.absolutePath /*+ ".mp3"*/)


            mMediaPlayer.prepare()
            mMediaPlayer.start()
            mMediaPlayer.setOnCompletionListener { mp: MediaPlayer? -> stop(onEndPlay) }
        } catch (e: Exception) {
            e.printStackTrace()
            GlobalScope.launch(Dispatchers.Main) {
                Toasty.error(context, R.string.something_went_wrong).show()
            }
        }
    }

    fun stop(onEndPlay: () -> Unit) {
        try {
            mMediaPlayer.stop()
            mMediaPlayer.reset()
        } catch (e: Exception) {
            e.printStackTrace()
            Toasty.error(context, R.string.something_went_wrong).show()
        }
        onEndPlay()
    }

    fun release() {
        mMediaPlayer.release()
    }

    fun init() {
        mMediaPlayer = MediaPlayer()
    }
}