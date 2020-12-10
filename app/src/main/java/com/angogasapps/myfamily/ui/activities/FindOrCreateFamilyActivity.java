package com.angogasapps.myfamily.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.ui.fragments.FindOrCreateFamilyFragment;

public class FindOrCreateFamilyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_or_create_family);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.findOrCreateFamilyDataContainer, new FindOrCreateFamilyFragment()).commit();

    }
}