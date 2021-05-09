package com.angogasapps.myfamily.ui.screens.main.holders;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.angogasapps.myfamily.databinding.VideoNewsViewHolderBinding;
import com.angogasapps.myfamily.models.Family;
import com.angogasapps.myfamily.models.NewsObject;
import com.bumptech.glide.Glide;

public class VideoNewsViewHolder extends BaseNewsViewHolder{
    private VideoNewsViewHolderBinding binding;

    public VideoNewsViewHolder(Context context, @NonNull View itemView) {
        super(context, itemView);
        binding = VideoNewsViewHolderBinding.bind(itemView);
    }

    @Override
    public void update(NewsObject newsObject) {
        binding.textName.setText(Family.getInstance().getNameByPhone(newsObject.getFromPhone()));
        binding.userPhoto.setImageBitmap(Family.getInstance().getMemberImageByPhone(newsObject.getFromPhone()));
//        Glide
//                .with(context)
//                .load(newsObject.getValue())
//                .into(binding.video);
    }
}
