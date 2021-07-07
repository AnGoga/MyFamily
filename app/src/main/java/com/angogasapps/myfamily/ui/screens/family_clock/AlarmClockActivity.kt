package com.angogasapps.myfamily.ui.screens.family_clock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.angogasapps.myfamily.databinding.ActivityAlamClockBinding
import com.angogasapps.myfamily.utils.StringFormater
import com.angogasapps.myfamily.utils.asDate

class AlarmClockActivity : AppCompatActivity() {
    lateinit var binding: ActivityAlamClockBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlamClockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setText()
        initOnClicks()
    }

    private fun setText() {
        binding.timeText.text = StringFormater.formatLongToTime(System.currentTimeMillis())
    }

    private fun initOnClicks() {
        binding.btn.setOnClickListener {
            finish()
        }
    }
}