package com.angogasapps.myfamily.ui.screens.family_settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.ActivityInviteUserBinding;
import com.angogasapps.myfamily.utils.FamilyManager;

public class InviteUserActivity extends AppCompatActivity {
    private ActivityInviteUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInviteUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.inviteText.setText(FamilyManager.getInviteLinkToFamily().toString());
        binding.copyBtn.setOnClickListener(v -> {

            ClipData clipData = ClipData.newPlainText("text", binding.inviteText.getText());
            ClipboardManager clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(clipData);

            Toast.makeText(this, R.string.copy_to_clipboard_manager, Toast.LENGTH_LONG).show();
        });
    }
}