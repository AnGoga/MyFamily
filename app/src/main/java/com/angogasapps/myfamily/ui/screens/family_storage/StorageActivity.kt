package com.angogasapps.myfamily.ui.screens.family_storage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.ActivityStorageBinding

class StorageActivity : AppCompatActivity() {
    lateinit var binding: ActivityStorageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recycleView
    }
}