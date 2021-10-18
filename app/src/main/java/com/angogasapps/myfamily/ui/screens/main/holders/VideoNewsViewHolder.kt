package com.angogasapps.myfamily.ui.screens.main.holders

import android.content.Context
import android.view.View
import com.angogasapps.myfamily.databinding.VideoNewsViewHolderBinding
import com.angogasapps.myfamily.ui.screens.main.holders.BaseNewsViewHolder
import com.angogasapps.myfamily.models.events.NewsObject
import com.angogasapps.myfamily.models.Family

class VideoNewsViewHolder(context: Context, itemView: View) :
    BaseNewsViewHolder(context, itemView) {
    private val binding: VideoNewsViewHolderBinding = VideoNewsViewHolderBinding.bind(itemView)
    override fun update(newsObject: NewsObject) {
        binding.textName.text = Family.getNameByPhone(newsObject.fromPhone)

        //        binding.userPhoto.setImageBitmap(Family.getInstance().getMemberImageByPhone(newsObject.getFromPhone()));
        //        Glide
        //                .with(context)
        //                .load(newsObject.getValue())
        //                .into(binding.video);
    }
}