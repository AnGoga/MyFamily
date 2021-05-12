package com.angogasapps.myfamily.ui.screens.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.async.LoadFamilyThread;
import com.angogasapps.myfamily.async.notification.FcmMessageManager;
import com.angogasapps.myfamily.databinding.ActivityMainBinding;
import com.angogasapps.myfamily.models.NewsEvent;
import com.angogasapps.myfamily.ui.screens.main.adapters.ItemTouchHelperCallback;
import com.angogasapps.myfamily.ui.screens.main.adapters.MainActivityAdapter;
import com.angogasapps.myfamily.ui.screens.main.adapters.NewsAdapter;
import com.angogasapps.myfamily.ui.screens.news_center.NewsManager;
import com.angogasapps.myfamily.ui.screens.personal_data.PersonalDataActivity;
import com.angogasapps.myfamily.utils.MainActivityUtils;

import java.util.Calendar;

import io.reactivex.disposables.Disposable;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainActivityAdapter adapter;
    private GridLayoutManager layoutManager;
    private ItemTouchHelper itemTouchHelper;
    private Disposable disposable;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new LoadFamilyThread(this).execute(USER);



        initToolbar();
        initRecyclerView();
        initNewsLayout();
        FcmMessageManager.subscribeToFamilyChat();
        MainActivityUtils.waitEndDownloadThread(this, newsAdapter);
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

    private void initNewsLayout() {
        newsAdapter = new NewsAdapter(this, NewsManager.getInstance().getAllNews());
        disposable = NewsManager.getInstance().subject().subscribe(event -> {
            if (NewsManager.getInstance().getAllNews().size() == 0){
                binding.viewPager.setBackgroundResource(R.drawable.default_family_emblem);
            }else if (NewsManager.getInstance().getAllNews().size() >= 1 &&
                    event.getEvent().equals(NewsEvent.ENewsEvents.added)){
                binding.viewPager.setBackgroundResource(0);
            }
            newsAdapter.update(event);
        });
        binding.viewPager.setAdapter(newsAdapter);
//        binding.viewPager.setClipToPadding(false);
//        binding.viewPager.setPadding(10,0,10,0);

        binding.viewPager.setOffscreenPageLimit(3);


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!disposable.isDisposed())
            disposable.dispose();
    }
}