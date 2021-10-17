package com.angogasapps.myfamily.ui.screens.family_settings

import androidx.appcompat.app.AppCompatActivity
import com.angogasapps.myfamily.ui.screens.family_settings.FamilyMembersAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import android.os.Bundle
import android.content.Intent
import com.angogasapps.myfamily.ui.screens.family_settings.InviteUserActivity
import android.graphics.Bitmap
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts
import android.graphics.BitmapFactory
import android.view.View
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.ActivityFamilySettingsBinding

class FamilySettingsActivity : AppCompatActivity() {
    private lateinit var mAdapter: FamilyMembersAdapter
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var binding: ActivityFamilySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFamilySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.inviteButton.setOnClickListener {
            startActivity(Intent(this, InviteUserActivity::class.java))
        }
        initRecycleView()
        setFamilyEmblemBitmap()
    }

    private fun initRecycleView() {
        mAdapter = FamilyMembersAdapter(this)
        mAdapter.reset()
        mLayoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = mAdapter
        binding.recycleView.layoutManager = mLayoutManager
    }

    private fun setFamilyEmblemBitmap() {
        var bitmap = FirebaseVarsAndConsts.familyEmblemImage
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.default_family_emblem)
        }
        binding.familyEmblem.setImageBitmap(bitmap)
    }
}