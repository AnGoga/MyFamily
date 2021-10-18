package com.angogasapps.myfamily.models

import android.content.Context
import com.angogasapps.myfamily.app.AppApplication.Companion.getInstance
import com.angogasapps.myfamily.models.Family
import android.graphics.Bitmap
import android.content.SharedPreferences
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.firebase.ROLE_MEMBER
import java.util.ArrayList

object Family {

    var usersList = ArrayList<User>()

    fun getMemberNameById(id: String): String {
        for ((id1, _, _, name) in usersList) {
            if (id1 == id) {
                return name
            }
        }
        return id
    }

    fun getMemberRoleById(id: String): String {
        for ((id1, _, _, _, _, _, role) in usersList) {
            if (id1 == id) {
                return role
            }
        }
        return ROLE_MEMBER
    }

    fun containsUserWithId(id: String): Boolean {
        var isContains = false
        for ((id1) in usersList) {
            if (id1 == id) {
                isContains = true
                break
            }
        }
        return isContains
    }

    fun getUserById(id: String): User? {
        for (user in usersList) {
            if (user.id == id) {
                return user
            }
        }
        return null
    }

    fun getNameByPhone(phone: String): String {
        for ((_, phone1, _, name) in usersList) {
            if (phone1 == phone) {
                return name
            }
        }
        return phone
    }

    fun getUserByPhone(phone: String): User? {
        for (user in usersList) {
            if (user.id == phone) {
                return user
            }
        }
        return null
    }

    fun getMemberImageByPhone(phone: String): Bitmap? {
        val user = getUserByPhone(phone)
        return if (user != null) user.userPhoto else User.default_user_photo
    }

    fun getPreferUserName(phone: String?): String? {
        val sf = getInstance()
            .getSharedPreferences("phone-name", Context.MODE_PRIVATE)
        return sf.getString(phone, phone)
    }

    fun getMemberImageById(id: String): Bitmap? {
        val user = getUserById(id)
        return if (user != null) user.userPhoto else User.default_user_photo
    }
}