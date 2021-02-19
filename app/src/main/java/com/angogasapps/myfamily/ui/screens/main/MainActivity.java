package com.angogasapps.myfamily.ui.screens.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.async.LoadFamilyThread;
import com.angogasapps.myfamily.database.AppDatabase;
import com.angogasapps.myfamily.database.DatabaseManager;
import com.angogasapps.myfamily.database.UserDao;
import com.angogasapps.myfamily.ui.customview.CardView;
import com.angogasapps.myfamily.ui.screens.chat.ChatActivity;
import com.angogasapps.myfamily.ui.screens.family_settings.FamilySettingsActivity;
import com.angogasapps.myfamily.ui.screens.personal_data.PersonalDataActivity;
import com.angogasapps.myfamily.utils.Others;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class MainActivity extends AppCompatActivity {
    CardView myFamilyCard, chatCard, cardStorage;
    Toolbar toolbar;
    CircleImageView userCircleImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        new LoadFamilyThread(this).execute(USER);

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        myFamilyCard = findViewById(R.id.mainActivityMyFamilyCard);
        chatCard = findViewById(R.id.mainActivityChatCard);
        cardStorage = findViewById(R.id.card_storage);
        toolbar = findViewById(R.id.main_toolbar);
        userCircleImageView = findViewById(R.id.main_toolbar_user_image);

        initToolbar();

        chatCard.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatActivity.class));
        });

        myFamilyCard.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FamilySettingsActivity.class));
        });

        cardStorage.setOnClickListener(v -> {

        });

    }

    private void initToolbar() {
        getSupportActionBar().hide();
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        if (USER.getUserPhoto() != null)
            userCircleImageView.setImageBitmap(USER.getUserPhoto());
        userCircleImageView.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, PersonalDataActivity.class));
        });
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