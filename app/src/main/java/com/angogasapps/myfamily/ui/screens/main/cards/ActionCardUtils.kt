package com.angogasapps.myfamily.ui.screens.main.cards

import com.angogasapps.myfamily.utils.stringOf
import com.angogasapps.myfamily.ui.screens.main.cards.ActionCardUtils.EActionCards
import com.angogasapps.myfamily.models.ActionCardState
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.ui.screens.chat.ChatActivity
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListActivity
import com.angogasapps.myfamily.ui.screens.family_clock.FamilyClockActivity
import com.angogasapps.myfamily.ui.screens.family_settings.FamilySettingsActivity
import com.angogasapps.myfamily.ui.screens.family_storage.SelectStorageActivity
import com.angogasapps.myfamily.ui.screens.news_center.NewsCenterActivity

object ActionCardUtils {
    enum class EActionCards {
        EVENT_CALENDAR, MY_DOINGS, CHAT, BUY_LIST, CLOCK, MY_FAMILY, STORAGE, NEWS_CENTER
    }

    @JvmStatic
    fun getState(card: EActionCards): ActionCardState {
        if (card == EActionCards.EVENT_CALENDAR) {
            return ActionCardState(
                stringOf(R.string.event_calendar),
                stringOf(R.string.event_calendar),
                R.drawable.mainActivityIcoCalendar, null, card
            )
        }
        if (card == EActionCards.MY_DOINGS) {
            return ActionCardState(
                stringOf(R.string.time_table),
                stringOf(R.string.time_table),
                R.drawable.mainActivityIcoTimeTable, null, card
            )
        }
        if (card == EActionCards.CHAT) {
            return ActionCardState(
                stringOf(R.string.chat),
                stringOf(R.string.chat),
                R.drawable.mainActivityIcoChat, ChatActivity::class.java, card
            )
        }
        if (card == EActionCards.BUY_LIST) {
            return ActionCardState(
                stringOf(R.string.buy_list),
                stringOf(R.string.buy_list),
                R.drawable.mainActivityIcoBuyList, BuyListActivity::class.java, card
            )
        }
        if (card == EActionCards.CLOCK) {
            return ActionCardState(
                stringOf(R.string.clock),
                stringOf(R.string.clock),
                R.drawable.ic_family_clock, FamilyClockActivity::class.java, card
            )
        }
        if (card == EActionCards.MY_FAMILY) {
            return ActionCardState(
                stringOf(R.string.my_family),
                stringOf(R.string.my_family),
                R.drawable.ic_my_family, FamilySettingsActivity::class.java, card
            )
        }
        if (card == EActionCards.STORAGE) {
            return ActionCardState(
                stringOf(R.string.storage),
                stringOf(R.string.storage),
                R.drawable.ic_family_storage, SelectStorageActivity::class.java, card
            )
        }
        return if (card == EActionCards.NEWS_CENTER) {
            ActionCardState(
                stringOf(R.string.news_center),
                stringOf(R.string.news_center),
                R.drawable.ic_news_cenetr, NewsCenterActivity::class.java, card
            )
        } else ActionCardState(
            stringOf(R.string.news_center),
            stringOf(R.string.news_center),
            R.drawable.ic_news_cenetr, NewsCenterActivity::class.java, card
        )
    }
}