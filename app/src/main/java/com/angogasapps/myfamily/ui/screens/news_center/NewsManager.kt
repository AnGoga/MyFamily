package com.angogasapps.myfamily.ui.screens.news_center

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.models.events.NewsEvent
import com.angogasapps.myfamily.models.events.NewsObject
import com.angogasapps.myfamily.network.interfaces.news_center.NewsCenterListener
import com.angogasapps.myfamily.utils.getIndexOfDeleteNews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsManager @Inject constructor(application: Application) : AndroidViewModel(application) {
    private val _flow = MutableSharedFlow<NewsEvent>()
    val flow
        get() = _flow.asSharedFlow()

    val allNews = ArrayList<NewsObject>()
    private val newsCenterListener: NewsCenterListener = appComponent.newsCenterListener

    init {
        initListener()
    }

    private fun initListener() {
        viewModelScope.launch(Dispatchers.IO) {
            newsCenterListener.listener.collect {
                when(it.event) {
                    NewsEvent.ENewsEvents.added -> {
                        allNews.add(it.value)
                        it.index = allNews.size - 1
                    }
                    NewsEvent.ENewsEvents.removed -> {
                        val index = getIndexOfDeleteNews(allNews, it.value)
                        if (index == -1) return@collect
                        allNews.removeAt(index)
                    }
                    NewsEvent.ENewsEvents.changed -> {}
                }
                _flow.emit(it)
            }
        }
    }
}