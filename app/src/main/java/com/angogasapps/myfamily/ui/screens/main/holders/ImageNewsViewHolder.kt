package com.angogasapps.myfamily.ui.screens.main.holders

import android.app.Activity
import android.view.View
import com.angogasapps.myfamily.ui.screens.main.holders.BaseNewsViewHolder
import com.angogasapps.myfamily.models.events.NewsObject
import com.angogasapps.myfamily.models.Family
import com.bumptech.glide.Glide
import com.angogasapps.myfamily.objects.ChatImageShower
import androidx.appcompat.app.AppCompatActivity
import com.angogasapps.myfamily.databinding.ImageNewsViewHolderBinding

class ImageNewsViewHolder(context: Activity, itemView: View)
    : BaseNewsViewHolder(context, itemView)
{
    private val binding: ImageNewsViewHolderBinding = ImageNewsViewHolderBinding.bind(itemView)
    override fun update(newsObject: NewsObject) {
        binding.textName.text = Family.getNameByPhone(newsObject.fromPhone)
        Glide
            .with(context)
            .load(newsObject.value)
            .into(binding.image)
    }

    init {
        binding.root.setOnClickListener { v: View? ->
            ChatImageShower((context as AppCompatActivity)).showImage(binding.image)
        }
    }
}