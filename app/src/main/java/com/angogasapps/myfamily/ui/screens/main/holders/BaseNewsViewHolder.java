package com.angogasapps.myfamily.ui.screens.main.holders;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.models.events.NewsObject;

public abstract class BaseNewsViewHolder extends RecyclerView.ViewHolder{
    protected Context context;
    public BaseNewsViewHolder(Context context, @NonNull View itemView) {
        super(itemView);
        this.context = context;
    }
    public abstract void update(NewsObject newsObject);
}
