package com.angogasapps.myfamily.ui.screens.main.cards

import com.angogasapps.myfamily.ui.screens.main.cards.ActionCardUtils.getState
import com.angogasapps.myfamily.models.ActionCardState
import com.angogasapps.myfamily.ui.screens.main.cards.ActionCardUtils.EActionCards
import com.angogasapps.myfamily.ui.screens.main.cards.ActionCardUtils
import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.utils.Async
import com.angogasapps.myfamily.async.LoadFamilyThread
import android.content.SharedPreferences
import com.angogasapps.myfamily.ui.screens.main.cards.MainActivityUtils
import kotlinx.coroutines.CoroutineScope
import java.lang.StringBuilder
import java.util.ArrayList

object MainActivityUtils {
    private fun getDefaultCardsArray(context: Context): ArrayList<ActionCardState> {
        val list = ArrayList<ActionCardState>()
        for (card in EActionCards.values()) {
            list.add(getState(card))
        }
        return list
    }

    fun waitEndDownloadThread(activity: Activity, adapter: RecyclerView.Adapter<*>, scope: CoroutineScope) {
        Async.runInNewThread {
            while (!LoadFamilyThread.isEnd) { }
            activity.runOnUiThread { adapter.notifyDataSetChanged() }
        }
    }

    fun savePreferCardPlaces(context: Context, list: ArrayList<ActionCardState>) {
        val sf = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = sf.edit()
        val str = StringBuilder()
        for (state in list) {
            str.append(state.cards.name).append(" ")
        }
        str.deleteAt(str.length - 1)
        editor.putString("card_place", str.toString())
        editor.apply()
    }

    fun getPreferCardsArray(context: Context): ArrayList<ActionCardState> {
        val sf = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val str = sf.getString("card_place", "")
        if (str == "") {
            return getDefaultCardsArray(context)
        }
        val list = ArrayList<ActionCardState>()
        for (card in str!!.split(" ".toRegex()).toTypedArray()) {
            list.add(getState(EActionCards.valueOf(card)))
        }
        return list
    }
}