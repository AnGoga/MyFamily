package com.angogasapps.myfamily.models

import com.angogasapps.myfamily.ui.screens.main.cards.ActionCardUtils.EActionCards

data class ActionCardState(
    var name: String,
    var subscript: String,
    var drawable: Int,
    var mClass: Class<*>?,
    var cards: EActionCards
)