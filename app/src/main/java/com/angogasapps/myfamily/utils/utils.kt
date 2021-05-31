package com.angogasapps.myfamily.utils

import java.text.SimpleDateFormat
import java.util.*


fun Long.asDate(): String{
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    return formatter.format(Date(this))
}