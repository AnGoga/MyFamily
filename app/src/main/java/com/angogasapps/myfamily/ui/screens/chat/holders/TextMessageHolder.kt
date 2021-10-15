package com.angogasapps.myfamily.ui.screens.chat.holders

import android.view.View
import com.angogasapps.myfamily.R
import android.widget.TextView

class TextMessageHolder(view: View) : AppBaseViewHolder(view) {

    override fun initLeftFields() {
        super.initLeftFields()
        (rootView.findViewById<View>(R.id.leftMessageText) as TextView).text = value
    }

    override fun initRightFields() {
        super.initRightFields()
        (rootView.findViewById<View>(R.id.rightMessageText) as TextView).text = value
    }
}