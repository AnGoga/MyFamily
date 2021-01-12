package com.angogasapps.myfamily.ui.screens.family_settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.utils.FamilyManager;

public class InviteUserActivity extends AppCompatActivity {
    TextView inviteLinkTextView;
    ImageView copyLinkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_user);

        inviteLinkTextView = findViewById(R.id.invite_link_text);
        copyLinkBtn = findViewById(R.id.copy_invite_link_btn);

        inviteLinkTextView.setText(FamilyManager.getInviteLinkToFamily().toString());
        copyLinkBtn.setOnClickListener(v -> {

            ClipData clipData = ClipData.newPlainText("text",inviteLinkTextView.getText());
            ClipboardManager clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(clipData);

            Toast.makeText(this, R.string.copy_to_clipboard_manager, Toast.LENGTH_LONG).show();
        });
    }
}