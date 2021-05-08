package com.angogasapps.myfamily.ui.screens.buy_list.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.BuyListHolderBinding;
import com.angogasapps.myfamily.models.BuyList;
import com.angogasapps.myfamily.models.BuyListEvent;
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListActivity;
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListManager;
import com.angogasapps.myfamily.ui.screens.buy_list.dialogs.ChangeOrDeleteBuyListDialog;

import java.util.ArrayList;

public class BuyListAdapter extends RecyclerView.Adapter<BuyListAdapter.BuyListHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<BuyList> buyListsArray;

    {
        buyListsArray = BuyListManager.getInstance().getBuyLists();
    }

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
        holder.binding.getRoot().setOnLongClickListener(v -> {
            (new ChangeOrDeleteBuyListDialog(context, buyListsArray.get(position))).show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return buyListsArray.size();
    }


    public void updateEnd(){
        notifyItemInserted(buyListsArray.size() - 1);
    }

    public void update(BuyListEvent event) {
        if (event.getEvent().equals(BuyListEvent.EBuyListEvents.buyListRemoved)){
            this.notifyItemRemoved(event.getIndex());
        }
        if (event.getEvent().equals(BuyListEvent.EBuyListEvents.buyListAdded)){
            this.notifyItemChanged(event.getIndex());
        }
        if (event.getEvent().equals(BuyListEvent.EBuyListEvents.buyListChanged)){
            this.notifyItemChanged(event.getIndex());
        }
    }

    public class BuyListHolder extends RecyclerView.ViewHolder {
        private BuyListHolderBinding binding;
        public BuyListHolder(@NonNull View itemView) {
            super(itemView);
            binding = BuyListHolderBinding.bind(itemView);

            binding.getRoot().setOnClickListener(v -> {
                BuyListActivity.getIGoToBuyListFragment().go(BuyListAdapter.this.buyListsArray.get(getLayoutPosition()));
            });
        }

        public void setTextName(String textName){
            binding.textName.setText(textName);
        }
    }
}
