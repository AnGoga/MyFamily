package com.angogasapps.myfamily.ui.screens.main.holders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.models.NewsObject;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class BaseNewsViewHolder extends RecyclerView.ViewHolder{
    protected Context context;
    public BaseNewsViewHolder(Context context, @NonNull View itemView) {
        super(itemView);
        this.context = context;
    }
    public abstract void update(NewsObject newsObject);
}
