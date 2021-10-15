package com.angogasapps.myfamily.ui.screens.personal_data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.databinding.ActivityPersonalDataBinding;
import com.angogasapps.myfamily.ui.screens.personal_dairy.PersonalDairyActivity;
import com.angogasapps.myfamily.ui.screens.settings.SettingsActivity;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;
import static com.angogasapps.myfamily.utils.UtilsKt.showInDevelopingToast;

public class PersonalDataActivity extends AppCompatActivity {
    private ActivityPersonalDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (USER.getUserPhoto() != null){
            binding.userImage.setImageBitmap(USER.getUserPhoto());
        }

        binding.settings.setOnClickListener(v -> {
            startActivity(new Intent(PersonalDataActivity.this, SettingsActivity.class));
        });

        OnClickListener plugClickListener = v -> {
            showInDevelopingToast();
        };

        binding.changePersonalData.setOnClickListener(plugClickListener);
        binding.personalDairy.setOnClickListener(v -> {
            startActivity(new Intent(PersonalDataActivity.this, PersonalDairyActivity.class));
        });


    }
}