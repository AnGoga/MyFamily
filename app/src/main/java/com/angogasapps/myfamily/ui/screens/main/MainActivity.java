package com.angogasapps.myfamily.ui.screens.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.async.LoadFamilyThread;
import com.angogasapps.myfamily.ui.customview.CardView;
import com.angogasapps.myfamily.ui.screens.chat.ChatActivity;
import com.angogasapps.myfamily.ui.screens.family_settings.FamilySettingsActivity;
import com.angogasapps.myfamily.ui.screens.findorcreatefamily.FindOrCreateFamilyActivity;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;
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

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_signout){
            AUTH.signOut();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}