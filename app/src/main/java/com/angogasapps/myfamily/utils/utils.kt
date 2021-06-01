package com.angogasapps.myfamily.utils

import android.text.Editable
import java.text.SimpleDateFormat
import java.util.*

private const val dateFormat = "dd/MM/yyyy"

fun Long.asDate(): String =  SimpleDateFormat(dateFormat).format(Date(this))

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

fun String.asMillis(): Long =  SimpleDateFormat(dateFormat).parse(this).time

