package com.angogasapps.myfamily.ui.screens.main.holders;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.angogasapps.myfamily.databinding.TextNewsViewHolderBinding;
import com.angogasapps.myfamily.models.Family;
import com.angogasapps.myfamily.models.events.NewsObject;

public class TextNewsViewHolder extends BaseNewsViewHolder{
    private TextNewsViewHolderBinding binding;

    public TextNewsViewHolder(Context context, @NonNull View itemView) {
        super(context, itemView);
        binding = TextNewsViewHolderBinding.bind(itemView);
    }

    @Override
    public void update(NewsObject newsObject) {
        binding.text.setText(newsObject.getValue());
        binding.textName.setText(Family.getInstance().getNameByPhone(newsObject.getFromPhone()));
    }
}
