package com.angogasapps.myfamily.models.buy_list

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class BuyListEvent(
        var index: Int = 0,
        var event: EBuyListEvents? = null,
        var buyListId: String = ""
) {
    enum class EBuyListEvents {
        productAdded, productRemoved, productChanged, buyListRemoved, buyListAdded, buyListChanged
    }
}