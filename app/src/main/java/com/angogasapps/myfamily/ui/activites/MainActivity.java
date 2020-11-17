package com.angogasapps.myfamily.ui.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.percentlayout.widget.PercentRelativeLayout;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.angogasapps.myfamily.R;

public class MainActivity extends AppCompatActivity {
    private PercentRelativeLayout chatCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatCard = findViewById(R.id.mainActivityChatCard);
        chatCard.setOnClickListener(view -> {

        });
    }

}