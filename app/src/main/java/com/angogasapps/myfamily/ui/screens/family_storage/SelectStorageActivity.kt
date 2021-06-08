package com.angogasapps.myfamily.ui.screens.family_storage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.angogasapps.myfamily.databinding.ActivitySelectStorageBinding
import com.angogasapps.myfamily.databinding.ActivityStorageBinding

class SelectStorageActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectStorageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initOnClicks()

    }

    private fun initOnClicks() {
        binding.cardImages.setOnClickListener {
            startActivity(Intent(this, StorageActivity::class.java)
//                    .also { it.putExtra("type", "images") }
            )

        }
    }
}