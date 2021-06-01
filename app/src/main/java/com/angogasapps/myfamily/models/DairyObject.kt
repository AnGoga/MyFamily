package com.angogasapps.myfamily.models

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class DairyObject(
        @PrimaryKey(autoGenerate = true) var key: Int, var title: String, var bodyText: String,
        var time: Long, var smile: String, var uri: String): Parcelable{



}
