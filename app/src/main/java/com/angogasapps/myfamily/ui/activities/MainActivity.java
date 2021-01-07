package com.angogasapps.myfamily.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.objects.LoadFamilyThread;
import com.angogasapps.myfamily.ui.customview.CardView;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.USER;

public class MainActivity extends AppCompatActivity {
    CardView myFamilyCard, chatCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (USER != null)
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

        chatCard.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatActivity.class));
        });

        myFamilyCard.setOnClickListener(v -> {

        });

    }
}