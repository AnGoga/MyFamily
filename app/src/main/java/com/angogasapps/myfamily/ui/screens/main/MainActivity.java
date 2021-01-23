package com.angogasapps.myfamily.ui.screens.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.async.LoadFamilyThread;
import com.angogasapps.myfamily.ui.customview.CardView;
import com.angogasapps.myfamily.ui.screens.chat.ChatActivity;
import com.angogasapps.myfamily.ui.screens.family_settings.FamilySettingsActivity;
import com.angogasapps.myfamily.ui.screens.findorcreatefamily.FindOrCreateFamilyActivity;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_USERS;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.STORAGE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class MainActivity extends AppCompatActivity {
    CardView myFamilyCard, chatCard, cardStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (USER.getFamily().equals("")) {
            startActivity(new Intent(this, FindOrCreateFamilyActivity.class));
            finish();
        }

        new LoadFamilyThread(this).execute(USER);

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        myFamilyCard = findViewById(R.id.mainActivityMyFamilyCard);
        chatCard = findViewById(R.id.mainActivityChatCard);
        cardStorage = findViewById(R.id.card_storage);

        chatCard.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatActivity.class));
        });

        myFamilyCard.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FamilySettingsActivity.class));
        });

        cardStorage.setOnClickListener(v -> {
            AUTH.signOut();
            finish();
        });



    }
}