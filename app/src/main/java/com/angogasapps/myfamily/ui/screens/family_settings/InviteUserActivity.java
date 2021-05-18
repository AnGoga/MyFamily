package com.angogasapps.myfamily.ui.screens.family_settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.ActivityInviteUserBinding;
import com.angogasapps.myfamily.utils.FamilyManager;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class InviteUserActivity extends AppCompatActivity {
    private ActivityInviteUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInviteUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.inviteText.setText(FamilyManager.getInviteLinkToFamily().toString());
        binding.identivicatorText.setText(USER.getFamily());
        binding.copyLinkBtn.setOnClickListener(v -> {
            copyToClipboardFromTextView(binding.inviteText);
        });

        binding.copyIdentivicatorBtn.setOnClickListener(v -> {
            copyToClipboardFromTextView(binding.identivicatorText);
        });
    }

    private void copyToClipboardFromTextView(TextView tv){
        ClipData clipData = ClipData.newPlainText("text", tv.getText());
        ClipboardManager clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(this, R.string.copy_to_clipboard_manager, Toast.LENGTH_LONG).show();
    }
}