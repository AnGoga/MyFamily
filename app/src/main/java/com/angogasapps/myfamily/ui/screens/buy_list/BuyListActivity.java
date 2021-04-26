package com.angogasapps.myfamily.ui.screens.buy_list;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.angogasapps.myfamily.databinding.ActivityByuListBinding;
import com.angogasapps.myfamily.models.BuyList;
import com.angogasapps.myfamily.ui.customview.buy_list_rv.BuyListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_BUY_LIST;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class BuyListActivity extends AppCompatActivity {
    private ActivityByuListBinding binding;
    private LinearLayoutManager layoutManager;
    private BuyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityByuListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();
        initBuyListListener();

        binding.floatingBtn.setOnClickListener(v -> {
            new AddBuyListDialog(BuyListActivity.this).show();
        });


    }


    private void initRecyclerView() {
        adapter = new BuyListAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        binding.recycleView.setLayoutManager(layoutManager);
        binding.recycleView.setAdapter(adapter);
    }


    private void initBuyListListener() {
        DATABASE_ROOT.child(NODE_BUY_LIST).child(USER.getFamily()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@androidx.annotation.NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.addBuyList(BuyList.from(snapshot));
            }

            @Override
            public void onChildChanged(@androidx.annotation.NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onChildRemoved(@androidx.annotation.NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@androidx.annotation.NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {}
        });
    }
}