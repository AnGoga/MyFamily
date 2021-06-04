package com.angogasapps.myfamily.ui.screens.family_storage

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



    }
}