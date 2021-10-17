package com.angogasapps.myfamily.ui.screens.news_center

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import com.angogasapps.myfamily.databinding.ActivityNewsCenterBinding
import com.angogasapps.myfamily.ui.screens.news_center.CreateNewNewsActivity

class NewsCenterActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewsCenterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createNewNewsBtn.setOnClickListener {
            startActivity(
                Intent(
                    this@NewsCenterActivity,
                    CreateNewNewsActivity::class.java
                )
            )
        }
        startActivity(Intent(this, CreateNewNewsActivity::class.java))
        finish()
    }
}