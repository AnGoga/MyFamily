package com.angogasapps.myfamily.ui.screens.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.ActionCardViewHolderBinding;
import com.angogasapps.myfamily.models.ActionCardState;


import java.util.ArrayList;
import java.util.Collections;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ActionCardHolder>
        implements ItemTouchHelperAdapter {
    private Context context;
    private LayoutInflater inflater;

    private ArrayList<ActionCardState> list = new ArrayList<>();

    public MainActivityAdapter(Context context, ArrayList<ActionCardState> list){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @NonNull
    @Override
    public ActionCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActionCardHolder(inflater.inflate(R.layout.action_card_view_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActionCardHolder holder, int position) {
        ActionCardState stats = list.get(position);
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
        swapList(fromPosition, toPosition);
        list.size();
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public static class ActionCardHolder extends RecyclerView.ViewHolder{
        private ActionCardViewHolderBinding binding;
        public ActionCardHolder(@NonNull View itemView) {
            super(itemView);
            binding = ActionCardViewHolderBinding.bind(itemView);
        }
        public void update(ActionCardState state){
            binding.card.update(state);
        }
    }

    public ArrayList<ActionCardState> getList() {
        return list;
    }

    private void swapList(int from, int to){
        if (from > to){ // справа налево
            for (int i = from; i > to; i--) {
               Collections.swap(list, i, i - 1);
            }
        }else{ //слева направо
            for (int i = from; i < to; i++){
                Collections.swap(list, i, i + 1);
            }
        }

    }

}
