package com.angogasapps.myfamily.ui.customview.news_center

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.models.QUOTE_ID
import com.angogasapps.myfamily.models.events.NewsEvent
import com.angogasapps.myfamily.ui.screens.main.cards.MainActivityUtils
import com.angogasapps.myfamily.ui.screens.news_center.NewsManager
import com.angogasapps.myfamily.ui.screens.news_center.QuoteManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsCenterLayout : ConstraintLayout {
    private lateinit var scope: CoroutineScope
    private val viewPager: ViewPager2
    private val tabLayout: TabLayout
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private lateinit var activity: Activity
    private lateinit var adapter: NewsAdapter
    @Inject
    lateinit var newsManager: NewsManager

    init {
        val root = inflater.inflate(R.layout.news_center_layout, this)
        viewPager = root.findViewById(R.id.view_pager)
        tabLayout = root.findViewById(R.id.tab_layout)
        appComponent.inject(this)
    }

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    fun setUpCenter(activity: Activity, scope: CoroutineScope){
        this.activity = activity
        this.scope = scope
        adapter = NewsAdapter(activity, newsManager.allNews)
        viewPager.adapter = adapter
        setupTabMediator()
        viewPager.offscreenPageLimit = 3
        showQuote()
        setupSubscribe()
        MainActivityUtils.waitEndDownloadThread(activity, adapter, scope)
    }

    private fun setupTabMediator(){
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->

        }.attach()
    }

    private fun setupSubscribe() {
        scope.launch {
            newsManager.flow.collect { event: NewsEvent ->
                if (newsManager.allNews.size == 0) {
                    adapter.update(event)
                    showQuote()
                    return@collect
                }
                if (newsManager.allNews.size == 2 && event.event == NewsEvent.ENewsEvents.added && newsManager.allNews[0].id == QUOTE_ID) {
                    hideQuote()
                }
                adapter.update(event)
            }
        }
    }

    private fun showQuote() {
        adapter.showQuote(QuoteManager.getRandomQuote())
    }

    private fun hideQuote() {
        adapter.hideQuote()
    }

    fun destroyCenter() { /* . . . */ }
}
