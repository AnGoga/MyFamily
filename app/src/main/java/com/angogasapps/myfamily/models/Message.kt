package com.angogasapps.myfamily.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.DataSnapshot

@Entity
data class Message(
    @PrimaryKey
    var id: String = "",
    var from: String = "",
    var type: String = "",
    var time: Long = 0,
    var value: String = ""
) : Comparable<Message> {

    override fun equals(other: Any?): Boolean {
        return if (other !is Message) false else id == other.id
    }

    constructor() : this(id = "") {}
    constructor(snapshot: DataSnapshot) : this() {
        val cash = snapshot.getValue(
            Message::class.java
        )
        id = snapshot.key!!
        from = cash!!.from
        type = cash.type
        time = cash.time
        value = cash.value
    }

    override fun compareTo(other: Message): Int {
        return when {
            time > other.time -> 1
            time < other.time -> -1
            else -> 0
        }
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + from.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}