package com.angogasapps.myfamily.ui.customview


import android.widget.LinearLayout
import android.widget.TextView
import com.angogasapps.myfamily.objects.ChatVoicePlayer
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.angogasapps.myfamily.R

class ChatVoiceMessageView : LinearLayout {
    private lateinit var fromNameView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var playButton: ImageView
    private lateinit var pauseButton: ImageView
    var messageKey: String? = null
    var voiceFileUrl: String? = null
    private var mVoicePlayer: ChatVoicePlayer? = null

    constructor(context: Context) : super(context) {
        initializeViews(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initializeViews(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initializeViews(context)
    }

    private fun initializeViews(context: Context) {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.voice_message_view, this)
    }

    fun setFromName(name: String?) {
        fromNameView.text = name
    }

    fun setTime(time: String?) {
        timeTextView.text = time
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        fromNameView = findViewById(R.id.voice_message_from_name_text)
        timeTextView = findViewById(R.id.voice_message_message_time_text)
        playButton = findViewById(R.id.voice_message_btn_play)
        pauseButton = findViewById(R.id.voice_message_btn_pause)
        mVoicePlayer = ChatVoicePlayer(context)
        mVoicePlayer!!.init()
        val onEndPlay = {
            playButton.setVisibility(VISIBLE)
            pauseButton.setVisibility(INVISIBLE)

        }
        playButton.setOnClickListener { v: View? ->
            mVoicePlayer!!.play(messageKey!!, voiceFileUrl!!, onEndPlay)
            playButton.setVisibility(INVISIBLE)
            pauseButton.setVisibility(VISIBLE)
        }
        pauseButton.setOnClickListener { v: View? ->
            mVoicePlayer!!.stop(onEndPlay)
            playButton.setVisibility(VISIBLE)
            pauseButton.setVisibility(INVISIBLE)
        }
    }
}