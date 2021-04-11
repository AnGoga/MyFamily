package com.angogasapps.myfamily.ui.customview.buy_list_rv;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.databinding.BuyListHolderBinding;
import com.angogasapps.myfamily.objects.BuyList;

public class BuyListHolder extends RecyclerView.ViewHolder {
    private BuyListHolderBinding binding;
    public BuyListHolder(@NonNull View itemView) {
        super(itemView);
        binding = BuyListHolderBinding.bind(itemView);
    }

    public void setTextName(String textName){
        binding.textName.setText(textName);
    }
}
