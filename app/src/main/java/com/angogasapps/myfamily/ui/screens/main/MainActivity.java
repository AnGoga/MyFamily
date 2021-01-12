package com.angogasapps.myfamily.ui.screens.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.async.TestLoadFamilyThread;
import com.angogasapps.myfamily.firebase.dynamic_links.DynamicLinksManager;
import com.angogasapps.myfamily.ui.customview.CardView;
import com.angogasapps.myfamily.ui.screens.chat.ChatActivity;
import com.angogasapps.myfamily.ui.screens.family_settings.FamilySettingsActivity;
import com.angogasapps.myfamily.ui.screens.findorcreatefamily.FindOrCreateFamilyActivity;

import java.util.HashMap;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class MainActivity extends AppCompatActivity {
    CardView myFamilyCard, chatCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if (USER.getFamily().equals("")) {
            startActivity(new Intent(this, FindOrCreateFamilyActivity.class));
            finish();
        }
//        new LoadFamilyThread(this).execute(USER);
        new TestLoadFamilyThread(this).execute(USER);

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        myFamilyCard = findViewById(R.id.mainActivityMyFamilyCard);
        chatCard = findViewById(R.id.mainActivityChatCard);

        chatCard.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatActivity.class));
        });

        myFamilyCard.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FamilySettingsActivity.class));
        });



    }
}