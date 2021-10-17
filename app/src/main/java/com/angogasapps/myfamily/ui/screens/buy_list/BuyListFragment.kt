package com.angogasapps.myfamily.ui.screens.buy_list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angogasapps.myfamily.databinding.FragmentBuyListBinding;
import com.angogasapps.myfamily.models.buy_list.BuyList;
import com.angogasapps.myfamily.models.buy_list.BuyListEvent;
import com.angogasapps.myfamily.ui.screens.buy_list.adapters.ProductsAdapter;
import com.angogasapps.myfamily.ui.screens.buy_list.dialogs.BuyListProductCreatorDialog;

import io.reactivex.disposables.Disposable;


public class BuyListFragment extends Fragment {
    private FragmentBuyListBinding binding;
    private BuyList buyList;
    private ProductsAdapter adapter;
    private LinearLayoutManager layoutManager;
    private Disposable observer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBuyListBinding.inflate(inflater, container, false);
        initOnClickListeners();
        initObserver();
        initRecyclerView();

//        binding.buyListName.setText(buyList.getName());

        getActivity().setTitle(buyList.getName());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (observer != null)
            if (!observer.isDisposed())
                observer.dispose();
    }

    private void initOnClickListeners() {
        binding.floatingBtn.setOnClickListener(v -> {
            (new BuyListProductCreatorDialog(getContext(), buyList.getId())).show();
        });
    }

    private void initObserver() {
        observer = BuyListManager.getInstance().subject.subscribe(event -> {
            if (event.getEvent().equals(BuyListEvent.EBuyListEvents.buyListRemoved)){
                if (event.getBuyListId().equals(this.buyList.getId())){
                    onRemoveThisBuyList();
                    return;
                }
            }
            if (!event.getBuyListId().equals(this.buyList.getId())) {
                return;
            }
            adapter.update(event);
        });
    }

    private void initRecyclerView() {
        adapter = new ProductsAdapter(getContext(), buyList);
        layoutManager = new LinearLayoutManager(getContext());
        binding.recycleView.setLayoutManager(layoutManager);
        binding.recycleView.setAdapter(adapter);
    }

    private void onRemoveThisBuyList() {
        BuyListActivity.getIgoToListOfBuyListsFragment().go();
    }

    public BuyList getBuyList() {
        return buyList;
    }

    public void setBuyList(BuyList buyList) {
        this.buyList = buyList;
    }
}