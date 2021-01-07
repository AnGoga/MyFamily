package com.angogasapps.myfamily.ui.screens.findorcreatefamily;

import androidx.appcompat.app.AppCompatActivity;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.ui.screens.findorcreatefamily.FindOrCreateFamilyFragment;

public class FindOrCreateFamilyActivity extends AppCompatActivity {
    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
    protected void onStart(){
        super.onStart();
        setContentView(R.layout.activity_find_or_create_family);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.findOrCreateFamilyDataContainer, new FindOrCreateFamilyFragment()).commit();

    }
}