package com.angogasapps.myfamily.ui.screens.family_settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.angogasapps.myfamily.utils.FamilyManager
import com.angogasapps.myfamily.firebase.*
import android.widget.TextView
import android.content.ClipData
import android.content.ClipboardManager
import android.view.View
import android.widget.Toast
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.ActivityInviteUserBinding

class InviteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInviteUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInviteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.inviteText.text = FamilyManager.inviteLinkToFamily.toString()
        binding.identivicatorText.text = USER.family
        binding.copyLinkBtn.setOnClickListener {
            copyToClipboardFromTextView(binding.inviteText)
        }
        binding.copyIdentivicatorBtn.setOnClickListener {
            copyToClipboardFromTextView(
                binding.identivicatorText
            )
        }
    }

    private fun copyToClipboardFromTextView(tv: TextView) {
        val clipData = ClipData.newPlainText("text", tv.text)
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, R.string.copy_to_clipboard_manager, Toast.LENGTH_LONG).show()
    }
}