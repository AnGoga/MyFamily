package com.angogasapps.myfamily.ui.screens.personal_dairy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.angogasapps.myfamily.databinding.ActivityDairyBuilderBinding
import com.angogasapps.myfamily.models.DairyObject
import com.angogasapps.myfamily.utils.asDate

class DairyBuilderActivity : AppCompatActivity() {
    lateinit var binding: ActivityDairyBuilderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDairyBuilderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dairy = intent.extras?.get("data") as? DairyObject?

        initFields(dairy)
    }

    private fun initFields(dairy: DairyObject?) {
        if (dairy != null) {

            binding.dateText.text = dairy.time.asDate()
        }

    }

}