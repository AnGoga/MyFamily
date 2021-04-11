package com.angogasapps.myfamily.ui.screens.buy_list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.ActivityByuListBinding;
import com.angogasapps.myfamily.objects.BuyList;
import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.ui.customview.buy_list_rv.BuyListAdapter;

import java.security.PublicKey;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class BuyListActivity extends AppCompatActivity {
    private ActivityByuListBinding binding;
    private LinearLayoutManager layoutManager;
    private BuyListAdapter adapter;
    private PublishSubject<BuyList> subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityByuListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();
        initSubject();

        binding.floatingBtn.setOnClickListener(v -> {
            new AddBuyListDialog(BuyListActivity.this, subject).show();
        });


    }

    private void initSubject() {
        Observer<BuyList> observer = new Observer<BuyList>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {}

            @Override
            public void onNext(@NonNull BuyList buyList) {
                BuyListActivity.this.runOnUiThread(() -> {
                    adapter.addBuyList(buyList);
                });
            }

            @Override
            public void onError(@NonNull Throwable e) {}
            @Override
            public void onComplete() {}
        };

        subject = PublishSubject.create();
        subject.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void initRecyclerView() {
        adapter = new BuyListAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        binding.recycleView.setLayoutManager(layoutManager);
        binding.recycleView.setAdapter(adapter);
    }
}