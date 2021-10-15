package com.angogasapps.myfamily.ui.screens.chat.holders

import com.angogasapps.myfamily.utils.StringFormater.formatLongToTime
import androidx.recyclerview.widget.RecyclerView
import android.widget.LinearLayout
import android.app.Activity
import android.content.Context
import com.angogasapps.myfamily.models.Family
import android.view.View
import com.angogasapps.myfamily.R
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView

open class AppBaseViewHolder(var rootView: View) : RecyclerView.ViewHolder(rootView) {
    protected var leftLayout: LinearLayout = itemView.findViewById(R.id.leftChatLayout)
    protected var rightLayout: LinearLayout = itemView.findViewById(R.id.rightChatLayout)
    protected var userAvatar: CircleImageView = itemView.findViewById(R.id.messageUserAvatar)

    protected lateinit var from: String
    protected lateinit var messageKey: String
    protected lateinit var value: String
    protected lateinit var time: String
    protected lateinit var name: String
    protected lateinit var context: Context


    open fun init(from: String, time: Long, messageKey: String, value: String, context: Activity) {
        this.from = from
        this.name = Family.getInstance().getMemberNameById(from)
        this.time = formatLongToTime(time)
        this.messageKey = messageKey
        this.value = value
        this.context = context
    }

    fun initLeftLayout() {
        leftLayout.visibility = View.VISIBLE
        rightLayout.visibility = View.INVISIBLE
        val bitmap = Family.getMemberImageById(from)
        userAvatar.setImageBitmap(bitmap)
        initLeftFields()
    }

    protected open fun initLeftFields() {
        (rootView.findViewById<View>(R.id.leftMessageFromName) as TextView).text = name
        (rootView.findViewById<View>(R.id.leftMessageTime) as TextView).text = time
    }

    fun initRightLayout() {
        rightLayout.visibility = View.VISIBLE
        leftLayout.visibility = View.INVISIBLE
        initRightFields()
    }

    protected open fun initRightFields() {
        (rootView.findViewById<View>(R.id.rightMessageFromName) as TextView).text = name
        (rootView.findViewById<View>(R.id.rightMessageTime) as TextView).text = time
    }
}