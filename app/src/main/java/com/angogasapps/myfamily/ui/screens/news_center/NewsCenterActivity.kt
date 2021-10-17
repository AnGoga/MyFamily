package com.angogasapps.myfamily.ui.screens.news_center;

import androidx.appcompat.app.AppCompatActivity;
import com.angogasapps.myfamily.databinding.ActivityNewsCenterBinding;

import android.content.Intent;
import android.os.Bundle;

import com.angogasapps.myfamily.R;

public class NewsCenterActivity extends AppCompatActivity {
    ActivityNewsCenterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsCenterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.createNewNewsBtn.setOnClickListener(v -> {
            startActivity(new Intent(NewsCenterActivity.this, CreateNewNewsActivity.class));
        });

        startActivity(new Intent(this, CreateNewNewsActivity.class));
        finish();
    }
}