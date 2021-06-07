package com.angogasapps.myfamily.ui.customview.news_center

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.models.QUOTE_ID
import com.angogasapps.myfamily.models.Quote
import com.angogasapps.myfamily.models.events.NewsEvent
import com.angogasapps.myfamily.models.events.NewsObject
import com.angogasapps.myfamily.models.events.NewsObject.TYPE_TEXT
import com.angogasapps.myfamily.ui.screens.main.holders.BaseNewsViewHolder
import com.angogasapps.myfamily.ui.screens.main.holders.ImageNewsViewHolder
import com.angogasapps.myfamily.ui.screens.main.holders.TextNewsViewHolder
import com.angogasapps.myfamily.ui.screens.main.holders.VideoNewsViewHolder
import java.util.*

class NewsAdapter(context: Activity, newsList: ArrayList<NewsObject>) : RecyclerView.Adapter<BaseNewsViewHolder>() {
    private val context: Activity
    private val inflater: LayoutInflater
    private var newsList = ArrayList<NewsObject>()

    init {
        this.newsList = newsList
        this.context = context
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseNewsViewHolder {
        if (viewType == 0) {
            return TextNewsViewHolder(context, inflater.inflate(R.layout.text_news_view_holder, parent, false))
        }
        if (viewType == 1) {
            return ImageNewsViewHolder(context, inflater.inflate(R.layout.image_news_view_holder, parent, false))
        }
        return if (viewType == 2) {
            VideoNewsViewHolder(context, inflater.inflate(R.layout.video_news_view_holder, parent, false))
        } else{
            VideoNewsViewHolder(context, inflater.inflate(R.layout.video_news_view_holder, parent, false))
        }
    }

    override fun onBindViewHolder(holder: BaseNewsViewHolder, position: Int) {
        val `object` = newsList[position]
        holder.update(`object`)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun getItemViewType(position: Int): Int {
        val type = newsList[position].type
        if (type == NewsObject.TYPE_TEXT) return 0
        if (type == NewsObject.TYPE_IMAGE) return 1
        return if (type == NewsObject.TYPE_VIDEO) 2 else -1
    }

    fun update(event: NewsEvent) {
        if (event.event == NewsEvent.ENewsEvents.added) {
            notifyItemChanged(event.index)
            return
        }
        if (event.event == NewsEvent.ENewsEvents.removed) {
            notifyItemRemoved(event.index)
            return
        }
        if (event.event == NewsEvent.ENewsEvents.changed) {
            notifyItemChanged(event.index)
            return
        }
    }

    fun showQuote(quote: Quote) {
        val new = NewsObject()
        new.timeCreate = Long.MAX_VALUE
        new.id = QUOTE_ID
        new.type = TYPE_TEXT
        new.value = quote.quote
        new.fromPhone = quote.by
        newsList.add(new)
        notifyItemInserted(0)
    }

    fun hideQuote() {
        newsList.removeAt(0)
        notifyItemRemoved(0)
    }

}
