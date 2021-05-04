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
                BuyListManager.this.onChildChanged(snapshot, previousChildName);
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

    public void removeProductInBuyList(BuyList buyList, int index){
        for (BuyList list : buyLists){
            if (list.getId().equals(buyList.getId())){
                list.getProducts().remove(index);
                return;
            }
        }
    }

    public void addProductToBuyList(BuyList buyList, BuyList.Product product) {
        for (BuyList list : buyLists) {
            if (list.getId().equals(buyList.getId())) {
                list.addProduct(product);
                return;
            }
        }
    }

    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName){
        synchronized (this) {
//                    BuyList newBuyList = BuyList.from(snapshot);
            BuyList newBuyList = BuyListUtils.parseBuyList(snapshot);


            for(BuyList list : buyLists){
                if (list.getId().equals(newBuyList.getId())){
                    BuyList oldBuyList = list;

                    if (newBuyList.getProducts().size() > oldBuyList.getProducts().size()){
                        // Добавлен новый продукт
                        oldBuyList.getProducts().add(newBuyList.getProducts().get(newBuyList.getProducts().size() - 1));
                    }else if(newBuyList.getProducts().size() < oldBuyList.getProducts().size()){
                        // Один продукт удалён
                        int index = BuyListUtils.getIndexOfRemoveProduct(oldBuyList.getProducts(), newBuyList.getProducts());
                        oldBuyList.getProducts().remove(index);
                    }else if(newBuyList.getProducts().size() == oldBuyList.getProducts().size()){
                        //Один продукт изменился

                    }
                    changedSubject.onNext(oldBuyList);
                    return;
                }
            }
        }
    }
}
