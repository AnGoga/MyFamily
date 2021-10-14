package com.angogasapps.myfamily.ui.screens.main.holders

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.models.events.NewsObject

abstract class BaseNewsViewHolder(protected var context: Context, itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    abstract fun update(newsObject: NewsObject)
}