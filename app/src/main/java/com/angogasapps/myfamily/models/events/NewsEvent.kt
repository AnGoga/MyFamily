package com.angogasapps.myfamily.models.events

class NewsEvent {
    var index = 0
    var event: ENewsEvents? = null
    var newsId: String? = null

    enum class ENewsEvents {
        added, removed, changed
    }
}