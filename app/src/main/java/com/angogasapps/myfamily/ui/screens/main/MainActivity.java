package com.angogasapps.myfamily.ui.screens.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.async.LoadFamilyThread;
import com.angogasapps.myfamily.async.notification.FcmMessageManager;
import com.angogasapps.myfamily.databinding.ActivityMainBinding;
import com.angogasapps.myfamily.ui.screens.personal_data.PersonalDataActivity;
import com.angogasapps.myfamily.ui.screens.settings.SettingsActivity;
import com.angogasapps.myfamily.utils.MainActivityUtils;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainActivityAdapter adapter;
    private GridLayoutManager layoutManager;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new LoadFamilyThread(this).execute(USER);


        initToolbar();
        initRecyclerView();
        FcmMessageManager.subscribeToFamilyChat();

    }

    private void initRecyclerView() {
        adapter = new MainActivityAdapter(this, MainActivityUtils.getArrayToView(this));
        layoutManager = new GridLayoutManager(this, 2);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.recycleView);

        binding.recycleView.setAdapter(adapter);
        binding.recycleView.setLayoutManager(layoutManager);
    }

    private void initToolbar() {
        getSupportActionBar().hide();
        binding.toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        if (USER.getUserPhoto() != null)
            binding.toolbarUserImage.setImageBitmap(USER.getUserPhoto());
        binding.toolbarUserImage.setOnClickListener(v -> {
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