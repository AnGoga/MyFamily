package com.angogasapps.myfamily.models.buy_list


class BuyListEvent {
    var index = 0
    var event: EBuyListEvents? = null
    var buyListId: String? = null

    enum class EBuyListEvents {
        productAdded, productRemoved, productChanged, buyListRemoved, buyListAdded, buyListChanged
    }
}