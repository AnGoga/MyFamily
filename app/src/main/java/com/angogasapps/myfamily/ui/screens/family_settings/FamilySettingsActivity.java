package com.angogasapps.myfamily.ui.screens.family_settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.ChatFunks;
import com.angogasapps.myfamily.ui.customview.family_members_rv.FamilyMembersAdapter;

public class FamilySettingsActivity extends AppCompatActivity {
    ImageView mFamilyEmblemImage;
    RecyclerView mRecyclerView;
    FamilyMembersAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_settings);

        mRecyclerView = findViewById(R.id.family_settings_recycleview);
        mFamilyEmblemImage = findViewById(R.id.family_settings_emblem);

        initRecycleView();

//        ChatFunks.downloadImageMessageAndSetBitmap();
    }

    private void initRecycleView(){
        mAdapter = new FamilyMembersAdapter(this);
        mAdapter.reset();
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}