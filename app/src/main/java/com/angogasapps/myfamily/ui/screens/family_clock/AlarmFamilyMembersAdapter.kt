package com.angogasapps.myfamily.ui.screens.family_clock

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.AlarmFamilyMemberHolderBinding
import com.angogasapps.myfamily.models.Family
import com.angogasapps.myfamily.models.User

class AlarmFamilyMembersAdapter(val context: Context): RecyclerView.Adapter<AlarmFamilyMembersAdapter.AlarmFamilyMembersHolder>() {
    val list: ArrayList<User>
    val inflater: LayoutInflater
    val checkedList: ArrayList<Boolean>

    init {
        list = Family.getInstance().usersList
        inflater = LayoutInflater.from(context)
        checkedList = ArrayList()
        for (i in 0 until list.size) checkedList.add(false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmFamilyMembersHolder {
        return AlarmFamilyMembersHolder(inflater.inflate(R.layout.alarm_family_member_holder, parent, false))
    }

    override fun onBindViewHolder(holder: AlarmFamilyMembersHolder, position: Int) {
        holder.update(list[position])
    }

    override fun getItemCount() = list.size

    fun getIdOfCheckedUsers(): ArrayList<String> {
        val res = ArrayList<String>()
       for (i in 0 until checkedList.size){
           if (checkedList[i]){
               res.add(list[i].id)
           }
       }
        return res
    }

    inner class AlarmFamilyMembersHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = AlarmFamilyMemberHolderBinding.bind(itemView)

        fun update(user: User) {
            binding.checkbox.isChecked = checkedList[position]
            binding.memberHolderUserName.text = Family.getInstance().getMemberNameById(user.id)
            user.userPhoto?.let {
                binding.memberHolderUserPhoto.setImageBitmap(it)
            }
            initOnClick()
        }

        private fun initOnClick() {
            binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                checkedList[position] = isChecked
            }
        }
    }
}