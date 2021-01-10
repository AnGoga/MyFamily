package com.angogasapps.myfamily.ui.screens.findorcreatefamily;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.ui.screens.findorcreatefamily.FindOrCreateFamilyFragment;

import java.util.Set;

public class FindOrCreateFamilyActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_or_create_family);
    }

    @Override
    protected void onStart(){
        super.onStart();

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        //TODO:
        /*
        Log.i("tag1", "\naction = " + action + "\ndata = " + data.toString());
        Log.i("tag", "Uri params = " + data.getQueryParameter("q"));
        Log.i("tag", "Uri params = " + data.getQueryParameter("w"));
        Log.i("tag", "Uri params = " + data.getQueryParameter("e"));
        Log.i("tag", "Uri params = " + data.getQueryParameter("r"));
        */

        getSupportFragmentManager().beginTransaction()
                .add(R.id.findOrCreateFamilyDataContainer, new FindOrCreateFamilyFragment()).commit();

    }
}