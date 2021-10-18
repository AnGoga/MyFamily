package com.angogasapps.myfamily.models

import com.angogasapps.myfamily.app.AppApplication.Companion.getInstance
import androidx.room.PrimaryKey
import android.graphics.Bitmap
import com.google.firebase.database.DataSnapshot
import android.graphics.BitmapFactory
import androidx.room.Entity
import androidx.room.Ignore
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.firebase.*

@Entity
data class User(
    @PrimaryKey
    var id: String = "",
    var phone: String = "",
    var family: String = "",
    var name: String = "",
    var birthday: Long = 0L,
    var photoURL: String = "",
    var role: String = "",
    var token: String = ""
) {

    @Ignore
    var userPhoto = BitmapFactory.decodeResource(
        getInstance().applicationContext.resources,
        R.drawable.ic_default_user_photo
    )
    get() = synchronized(this) { return if (userPhoto != null) userPhoto!! else default_user_photo!! }
    public set

    companion object {
        @JvmField
        val default_user_photo: Bitmap? = null

        @JvmStatic
        fun from(snapshot: DataSnapshot): User {
            val user = snapshot.getValue(
                User::class.java
            )
            user!!.id = snapshot.key!!
            return user
        }
    }

    fun setBitmap(bitmap: Bitmap?) {
        userPhoto = bitmap
    }

    fun haveFamily(): Boolean {
        return family != ""
    }
}