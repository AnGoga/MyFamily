package com.angogasapps.myfamily.ui.screens.buy_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.BuyListHolderBinding;
import com.angogasapps.myfamily.models.BuyList;

import java.util.ArrayList;

public class BuyListAdapter extends RecyclerView.Adapter<BuyListAdapter.BuyListHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<BuyList> buyListsArray = new ArrayList<>();

    public BuyListAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BuyListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BuyListHolder(inflater.inflate(R.layout.buy_list_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BuyListHolder holder, int position) {
        holder.setTextName(buyListsArray.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return buyListsArray.size();
    }

    public void addBuyList(BuyList buyList){
        buyListsArray.add(buyList);
        notifyItemInserted(buyListsArray.size() - 1);
    }

    public static class BuyListHolder extends RecyclerView.ViewHolder {
        private BuyListHolderBinding binding;
        public BuyListHolder(@NonNull View itemView) {
            super(itemView);
            binding = BuyListHolderBinding.bind(itemView);
        }

        public void setTextName(String textName){
            binding.textName.setText(textName);
        }
    }
}
