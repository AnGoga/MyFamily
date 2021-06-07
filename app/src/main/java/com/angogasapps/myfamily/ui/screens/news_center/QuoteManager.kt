package com.angogasapps.myfamily.ui.screens.news_center

import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.models.Quote
import kotlin.random.Random

class QuoteManager private constructor() {
    lateinit var list: List<String>

    init {
        init()
    }

    private fun init(){
        list = AppApplication.getInstance().resources.getStringArray(R.array.quotes).asList()
    }

    fun getRandomQuote(): Quote{
        val int = Random.nextInt(0, list.size / 2 - 1) * 2
        return Quote(list[int], list[int + 1])
    }


    companion object{
        private var manager: QuoteManager? = null
        fun getInstance(): QuoteManager{
            if (manager == null)
                manager = QuoteManager()
            return manager!!
        }
    }
}