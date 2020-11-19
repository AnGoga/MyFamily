package com.angogasapps.myfamily.ui.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.percentlayout.widget.PercentRelativeLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.ui.customview.CardView;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.AUTH;

public class MainActivity extends AppCompatActivity {
    private CardView chatCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        chatCard = findViewById(R.id.mainActivityChatCard);
        chatCard.setOnClickListener(view -> {
            AUTH.signOut();
        });

    }

}