package com.angogasapps.myfamily.ui.screens.main.holders;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.databinding.ImageNewsViewHolderBinding;
import com.angogasapps.myfamily.models.Family;
import com.angogasapps.myfamily.models.NewsObject;
import com.angogasapps.myfamily.objects.ChatImageShower;
import com.bumptech.glide.Glide;

public class ImageNewsViewHolder extends BaseNewsViewHolder{
    private ImageNewsViewHolderBinding binding;

    public ImageNewsViewHolder(Activity context, @NonNull View itemView) {
        super(context, itemView);
        binding = ImageNewsViewHolderBinding.bind(itemView);

        binding.getRoot().setOnClickListener(v -> {
            new ChatImageShower((AppCompatActivity) context).showImage(binding.image);
        });
    }

    @Override
    public void update(NewsObject newsObject) {
        binding.textName.setText(Family.getInstance().getNameByPhone(newsObject.getFromPhone()));
        binding.userPhoto.setImageBitmap(Family.getInstance().getMemberImageByPhone(newsObject.getFromPhone()));
        Glide
                .with(context)
                .load(newsObject.getValue())
                .into(binding.image);
    }
}
