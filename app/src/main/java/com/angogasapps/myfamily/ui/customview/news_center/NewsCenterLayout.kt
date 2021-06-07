package com.angogasapps.myfamily.ui.customview.news_center

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.models.QUOTE_ID
import com.angogasapps.myfamily.models.events.NewsEvent
import com.angogasapps.myfamily.ui.screens.main.cards.MainActivityUtils
import com.angogasapps.myfamily.ui.screens.news_center.NewsManager
import com.angogasapps.myfamily.ui.screens.news_center.QuoteManager
import io.reactivex.disposables.Disposable

class NewsCenterLayout : ConstraintLayout {
    private val viewPager: ViewPager2
    private val inflater: LayoutInflater
    private lateinit var activity: Activity
    private lateinit var adapter: NewsAdapter
    private var disposable: Disposable? = null
    init {
        inflater = LayoutInflater.from(context)
        val root = inflater.inflate(R.layout.news_center_layout, this)
        viewPager = root.findViewById(R.id.view_pager)
        QuoteManager.getInstance()
    }

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    fun setUpCenter(activity: Activity){
        this.activity = activity
        adapter = NewsAdapter(activity, NewsManager.getInstance().allNews)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3
        showQuote()

        disposable = NewsManager.getInstance().subject().subscribe { event: NewsEvent ->
            if (NewsManager.getInstance().allNews.size == 0) {
                adapter.update(event)
                showQuote()
                return@subscribe
            }
            if (NewsManager.getInstance().allNews.size == 2 && event.event == NewsEvent.ENewsEvents.added && NewsManager.getInstance().allNews[0].id == QUOTE_ID) {
                hideQuote()
            }
            adapter.update(event)
        }

        MainActivityUtils.waitEndDownloadThread(activity, adapter)
    }

    private fun showQuote() {
        adapter.showQuote(QuoteManager.getInstance().getRandomQuote())
    }

    private fun hideQuote() {
        adapter.hideQuote()
    }

    fun destroyCenter(){
        if (!disposable?.isDisposed!!)
            disposable?.dispose()
    }


}
