package com.angogasapps.myfamily.utils

import android.text.Editable
import java.text.SimpleDateFormat
import java.util.*

private const val dateFormat = "dd/MM/yyyy"

fun Long.asDate(): String = SimpleDateFormat(dateFormat).format(Date(this))
//        .split("/").toMutableList().also { it[1] = it[1] + 1 }.joinToString("/")

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

fun String.asMillis(): Long =  SimpleDateFormat(dateFormat).parse(this).time

fun toDate(year: Int, month: Int, day: Int): String{
    return "$day/$month/$year"
}

