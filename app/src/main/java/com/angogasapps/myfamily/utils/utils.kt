package com.angogasapps.myfamily.utils

import android.text.Editable
import com.angogasapps.myfamily.models.DairyObject
import com.google.firebase.database.DataSnapshot
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val dateFormat = "dd/MM/yyyy"

fun Long.asDate(): String = SimpleDateFormat(dateFormat).format(Date(this))
//        .split("/").toMutableList().also { it[1] = it[1] + 1 }.joinToString("/")

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

fun String.asMillis(): Long =  SimpleDateFormat(dateFormat).parse(this).time

fun toDate(year: Int, month: Int, day: Int): String{
    return "$day/$month/$year"
}

fun ArrayList<DairyObject>.haveWithKey(dairy: DairyObject): Boolean{
    return this.indexOfThisKey(dairy) >= 0
}

fun ArrayList<DairyObject>.indexOfThisKey(dairy: DairyObject): Int{
    this.forEachIndexed { index, it -> run {
        if (it.key == dairy.key)
            return index
    }}
    return -1
}

fun DataSnapshot.asString(): String? = this.getValue(String::class.java)

