package com.angogasapps.myfamily.ui.screens.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.async.LoadFamilyThread;
import com.angogasapps.myfamily.async.ServiceManager;
import com.angogasapps.myfamily.async.notification.FcmMessageManager;

import com.angogasapps.myfamily.databinding.ActivityMainDeprecatedBinding;
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListActivity;
import com.angogasapps.myfamily.ui.screens.chat.ChatActivity;
import com.angogasapps.myfamily.ui.screens.family_settings.FamilySettingsActivity;
import com.angogasapps.myfamily.ui.screens.personal_data.PersonalDataActivity;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class DeprecatedMainActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    private ActivityMainDeprecatedBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        new LoadFamilyThread(this).execute(USER);

        ServiceManager.checkServices(this);

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding = ActivityMainDeprecatedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initToolbar();

        binding.chatCard.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatActivity.class));
        });

        binding.familyCard.setOnClickListener(v -> {
            startActivity(new Intent(DeprecatedMainActivity.this, FamilySettingsActivity.class));
        });

        binding.cardStorage.setOnClickListener(v -> {
            startActivity(new Intent(DeprecatedMainActivity.this, MainActivity.class));
        });

        binding.buyListCard.setOnClickListener(v -> {
            startActivity(new Intent(DeprecatedMainActivity.this, BuyListActivity.class));
        });

        FcmMessageManager.subscribeToFamilyChat();


    }

    private void initToolbar() {
        getSupportActionBar().hide();
        binding.toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        if (USER.getUserPhoto() != null)
            binding.toolbarUserImage.setImageBitmap(USER.getUserPhoto());
        binding.toolbarUserImage.setOnClickListener(v -> {
            startActivity(new Intent(DeprecatedMainActivity.this, PersonalDataActivity.class));
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