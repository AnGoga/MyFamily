package com.angogasapps.myfamily.models.events

class NewsEvent {
    var index = 0
    var event: ENewsEvents = ENewsEvents.added
    var value: NewsObject = NewsObject()

    enum class ENewsEvents {
        added, removed, changed
    }
}