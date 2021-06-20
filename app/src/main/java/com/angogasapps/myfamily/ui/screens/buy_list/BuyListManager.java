package com.angogasapps.myfamily.ui.screens.buy_list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.angogasapps.myfamily.models.buy_list.BuyList;
import com.angogasapps.myfamily.models.buy_list.BuyListEvent;
import com.angogasapps.myfamily.utils.BuyListUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_BUY_LIST;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class BuyListManager {
    private static BuyListManager buyListManager;

    private volatile ArrayList<BuyList> buyLists = new ArrayList<>();

    PublishSubject<BuyListEvent> subject = PublishSubject.create();

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
        subject
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

                    BuyListEvent event = new BuyListEvent();
                    event.setIndex(buyLists.size() - 1);
                    event.setEvents(BuyListEvent.EBuyListEvents.buyListAdded);
                    event.setBuyListId(buyList.getId());
                    subject.onNext(event);
                }
            }

            @Override
            public void  onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                BuyListManager.this.onChildChanged(snapshot, previousChildName);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                BuyList buyList = BuyListUtils.parseBuyList(snapshot);

                BuyListEvent event = new BuyListEvent();
                event.setBuyListId(buyList.getId());
                event.setEvents(BuyListEvent.EBuyListEvents.buyListRemoved);
                for (int i = 0; i < buyLists.size(); i++) {
                    if (buyLists.get(i).getId().equals(buyList.getId())){
                        event.setIndex(i);
                        buyLists.remove(i);
                        break;
                    }
                }
                subject.onNext(event);
            }

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
            BuyList newBuyList = BuyListUtils.parseBuyList(snapshot);
            BuyListEvent event = new BuyListEvent();

//            for(BuyList oldBuyList : buyLists){
            for(int i = 0; i < buyLists.size(); i++){
                BuyList oldBuyList = buyLists.get(i);



                if (oldBuyList.getId().equals(newBuyList.getId())){


                    if (!oldBuyList.getName().equals(newBuyList.getName())) {
                        oldBuyList.setName(newBuyList.getName());
                        event.setBuyListId(newBuyList.getId());
                        event.setIndex(i);
                        event.setEvents(BuyListEvent.EBuyListEvents.buyListChanged);
                        subject.onNext(event);
                        return;
                    }

                    int index = 0;
                    if (newBuyList.getProducts().size() > oldBuyList.getProducts().size()){
                        // Добавлен новый продукт
                        oldBuyList.getProducts().add(newBuyList.getProducts().get(newBuyList.getProducts().size() - 1));
                        index = oldBuyList.getProducts().size() - 1;

                        event.setEvents(BuyListEvent.EBuyListEvents.productAdded);
                    }else if(newBuyList.getProducts().size() < oldBuyList.getProducts().size()){
                        // Один продукт удалён
                        index = BuyListUtils.getIndexOfRemoveProduct(oldBuyList.getProducts(), newBuyList.getProducts());
                        oldBuyList.getProducts().remove(index);
                        event.setEvents(BuyListEvent.EBuyListEvents.productRemoved);
                    }else {//if(newBuyList.getProducts().size() == oldBuyList.getProducts().size()){
                        //Один продукт изменился
                        index = BuyListUtils.getIndexOfChangeProduct(oldBuyList.getProducts(), newBuyList.getProducts());
                        oldBuyList.getProducts().set(index, newBuyList.getProducts().get(index));
                        event.setEvents(BuyListEvent.EBuyListEvents.productChanged);
                    }
                    event.setIndex(index);
                    event.setBuyListId(newBuyList.getId());

                    subject.onNext(event);
                    return;
                }

            }
        }
    }
}
