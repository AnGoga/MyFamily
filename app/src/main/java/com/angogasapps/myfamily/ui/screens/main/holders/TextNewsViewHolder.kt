package com.angogasapps.myfamily.ui.screens.main.holders

import android.content.Context
import android.view.View
import com.angogasapps.myfamily.databinding.TextNewsViewHolderBinding
import com.angogasapps.myfamily.ui.screens.main.holders.BaseNewsViewHolder
import com.angogasapps.myfamily.models.events.NewsObject
import com.angogasapps.myfamily.models.Family

class TextNewsViewHolder(context: Context, itemView: View) : BaseNewsViewHolder(
    context, itemView
) {
    private val binding: TextNewsViewHolderBinding = TextNewsViewHolderBinding.bind(itemView)
    override fun update(newsObject: NewsObject) {
        binding.text.text = newsObject.value
        binding.textName.text = Family.getInstance().getNameByPhone(newsObject.fromPhone)
    }

}