package com.angogasapps.myfamily.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.DataSnapshot

@Entity
class Message : Comparable<Message> {
    @PrimaryKey
    var id = ""
    var from: String? = null
    var type: String? = null
    var time: Long = 0
    var value: String? = null
    override fun equals(obj: Any?): Boolean {
        return if (obj !is Message) false else id == obj.id
    }

    //    public boolean equals(Message obj) {
    //        return (this.id.equals(obj.getId()));
    //    }
    constructor() {}
    constructor(snapshot: DataSnapshot) {
        val cash = snapshot.getValue(
            Message::class.java
        )
        id = snapshot.key!!
        from = cash!!.from
        type = cash.type
        time = cash.time
        value = cash.value
    }

    override fun compareTo(obj: Message): Int {
        return if (time > obj.time) {
            1
        } else if (time < obj.time) {
            -1
        } else {
            0
        }
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (from?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + time.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        return result
    }
}