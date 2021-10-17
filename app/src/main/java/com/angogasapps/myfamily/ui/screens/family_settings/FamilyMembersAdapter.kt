package com.angogasapps.myfamily.ui.screens.family_settings

import android.content.Context
import com.angogasapps.myfamily.utils.StringFormater.formatToRole

import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.ui.screens.family_settings.FamilyMembersAdapter.FamilyMembersHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.models.Family
import android.widget.TextView
import com.angogasapps.myfamily.utils.StringFormater
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import com.angogasapps.myfamily.models.User
import java.util.ArrayList

class FamilyMembersAdapter(var context: Context) : RecyclerView.Adapter<FamilyMembersHolder>() {
    var inflater: LayoutInflater = LayoutInflater.from(context)
    var mFamilyMembersList = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamilyMembersHolder {
        return FamilyMembersHolder(inflater.inflate(R.layout.family_member_holder, parent, false))
    }

    override fun onBindViewHolder(holder: FamilyMembersHolder, position: Int) {
        holder.initHolder(mFamilyMembersList[position], context)
    }

    override fun getItemCount(): Int {
        return mFamilyMembersList.size
    }

    fun reset() {
        mFamilyMembersList = ArrayList()
        mFamilyMembersList.addAll(Family.getInstance().usersList)
        notifyDataSetChanged()
    }

    class FamilyMembersHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userNameTextView: TextView = itemView.findViewById(R.id.member_holder_user_name)
        var userRoleTextView: TextView = itemView.findViewById(R.id.member_holder_user_role)
        var userImage: ImageView = itemView.findViewById(R.id.member_holder_user_photo)

        fun initHolder(user: User, context: Context?) {
            userNameTextView.text = Family.getInstance().getMemberNameById(user.id)
            userRoleTextView.text = formatToRole(Family.getInstance().getMemberRoleById(user.id))
            Family.getInstance().getUserById(user.id)!!.userPhoto?.let {
                userImage.setImageBitmap(it)
            }
        }

    }

}