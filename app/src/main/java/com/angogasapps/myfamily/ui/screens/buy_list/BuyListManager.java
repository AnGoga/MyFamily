package com.angogasapps.myfamily.ui.screens.buy_list;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.angogasapps.myfamily.models.BuyList;
import com.angogasapps.myfamily.utils.BuyListUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_BUY_LIST;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class BuyListManager {
    private static BuyListManager buyListManager;

    private volatile ArrayList<BuyList> buyLists = new ArrayList<>();

    PublishSubject<BuyList> addedSubject = PublishSubject.create();
    PublishSubject<BuyList> changedSubject = PublishSubject.create();

    private ChildEventListener listener;

    {
        initSubjects();
        initListener();
    }


    public static BuyListManager getInstance(){
        synchronized (BuyListManager.class){
            if (buyListManager == null)
                buyListManager = new BuyListManager();
            return buyListManager;
        }
    }

    private void initSubjects() {
        addedSubject
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread());
        changedSubject
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread());
    }


    private void initListener() {
        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                synchronized (this) {
//                    BuyList buyList = BuyList.from(snapshot);
                    BuyList buyList = BuyListUtils.parseBuyList(snapshot);
                    buyLists.add(buyList);
                    addedSubject.onNext(buyList);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                synchronized (this) {
//                    BuyList newBuyList = BuyList.from(snapshot);
                    BuyList newBuyList = BuyListUtils.parseBuyList(snapshot);
                    changedSubject.onNext(newBuyList);

                    for (int i = 0; i < buyLists.size(); i++){
                        BuyList oldBuyList = buyLists.get(i);
                        if (oldBuyList.getId().equals(newBuyList.getId())){
                            buyLists.set(i, newBuyList);
                            changedSubject.onNext(newBuyList);
                            return;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.toException().printStackTrace();
            }
        };

        DATABASE_ROOT.child(NODE_BUY_LIST).child(USER.getFamily()).addChildEventListener(listener);

    }

    public ArrayList<BuyList> getBuyLists() {
        return buyLists;
    }


}
