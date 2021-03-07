package com.angogasapps.myfamily.ui.screens.family_settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.ActivityFamilySettingsBinding;
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts;
import com.angogasapps.myfamily.ui.customview.family_members_rv.FamilyMembersAdapter;

public class FamilySettingsActivity extends AppCompatActivity {

    FamilyMembersAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    private ActivityFamilySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFamilySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.inviteButton.setOnClickListener(v -> {
            startActivity(new Intent(this, InviteUserActivity.class));
        });

        initRecycleView();
        setFamilyEmblemBitmap();
    }

    private void initRecycleView(){
        mAdapter = new FamilyMembersAdapter(this);
        mAdapter.reset();
        mLayoutManager = new LinearLayoutManager(this);

        binding.recycleView.setAdapter(mAdapter);
        binding.recycleView.setLayoutManager(mLayoutManager);
    }

    private void setFamilyEmblemBitmap() {
        Bitmap bitmap = FirebaseVarsAndConsts.familyEmblemImage;
        if (bitmap == null){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_family_emblem);
        }
        binding.familyEmblem.setImageBitmap(bitmap);
    }
}



