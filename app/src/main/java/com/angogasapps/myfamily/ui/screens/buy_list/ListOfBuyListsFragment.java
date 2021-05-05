package com.angogasapps.myfamily.ui.screens.buy_list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angogasapps.myfamily.databinding.FragmentListOfBuyListsBinding;
import com.angogasapps.myfamily.ui.screens.buy_list.adapters.BuyListAdapter;
import com.angogasapps.myfamily.ui.screens.buy_list.dialogs.AddBuyListDialog;

import io.reactivex.disposables.Disposable;

public class ListOfBuyListsFragment extends Fragment {
    private FragmentListOfBuyListsBinding binding;
    private LinearLayoutManager layoutManager;
    private BuyListAdapter adapter;
    private Disposable disposable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListOfBuyListsBinding.inflate(inflater, container, false);


        initOnClickListeners();
        initObserver();
        initRecyclerView();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (disposable != null)
            if (!disposable.isDisposed())
                disposable.dispose();
    }

    private void initOnClickListeners() {
        binding.floatingBtn.setOnClickListener(v -> {
            new AddBuyListDialog(getContext()).show();
        });
    }

    private void initRecyclerView() {
        adapter = new BuyListAdapter(getContext());
        layoutManager = new LinearLayoutManager(getContext());
        binding.recycleView.setLayoutManager(layoutManager);
        binding.recycleView.setAdapter(adapter);
    }

    private void initObserver() {
        disposable = BuyListManager.getInstance().changedSubject.subscribe(event->{
            adapter.update(event);
        });
    }
}