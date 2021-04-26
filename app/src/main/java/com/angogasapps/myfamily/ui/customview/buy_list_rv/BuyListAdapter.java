package com.angogasapps.myfamily.ui.customview.buy_list_rv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.models.BuyList;

import java.util.ArrayList;

public class BuyListAdapter extends RecyclerView.Adapter<BuyListHolder> {
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
}
