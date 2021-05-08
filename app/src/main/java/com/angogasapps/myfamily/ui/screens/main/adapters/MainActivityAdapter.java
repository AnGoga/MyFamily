package com.angogasapps.myfamily.ui.screens.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.MainCardViewHolderBinding;
import com.angogasapps.myfamily.models.MainCardState;


import java.util.ArrayList;
import java.util.Collections;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.MainCardHolder>
        implements ItemTouchHelperAdapter {
    private Context context;
    private LayoutInflater inflater;

    private ArrayList<MainCardState> list = new ArrayList<>();

    public MainActivityAdapter(Context context, ArrayList<MainCardState> list){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @NonNull
    @Override
    public MainCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainCardHolder(inflater.inflate(R.layout.main_card_view_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainCardHolder holder, int position) {
        MainCardState stats = list.get(position);
        holder.update(stats);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onItemDismiss(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(list, fromPosition, toPosition);
        list.size();
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public static class MainCardHolder extends RecyclerView.ViewHolder{
        private MainCardViewHolderBinding binding;
        public MainCardHolder(@NonNull View itemView) {
            super(itemView);
            binding = MainCardViewHolderBinding.bind(itemView);
        }
        public void update(MainCardState state){
            binding.card.update(state);
        }
    }
}
