package com.angogasapps.myfamily.ui.screens.personal_data

import com.angogasapps.myfamily.utils.showInDevelopingToast
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.angogasapps.myfamily.firebase.*
import android.content.Intent
import android.view.View
import com.angogasapps.myfamily.databinding.ActivityPersonalDataBinding
import com.angogasapps.myfamily.ui.screens.settings.SettingsActivity
import com.angogasapps.myfamily.ui.screens.personal_dairy.PersonalDairyActivity

class PersonalDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersonalDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (USER.userPhoto != null) {
            binding.userImage.setImageBitmap(USER.userPhoto)
        }
        binding.settings.setOnClickListener {
            startActivity(
                Intent(this@PersonalDataActivity, SettingsActivity::class.java)
            )
        }
        val plugClickListener = View.OnClickListener { showInDevelopingToast() }
        binding.changePersonalData.setOnClickListener(plugClickListener)
        binding.personalDairy.setOnClickListener {
            startActivity(
                Intent(this@PersonalDataActivity, PersonalDairyActivity::class.java)
            )
        }
    }
}