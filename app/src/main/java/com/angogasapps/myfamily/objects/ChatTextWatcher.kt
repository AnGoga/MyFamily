package com.angogasapps.myfamily.objects

import android.widget.EditText
import android.text.TextWatcher
import android.text.Editable
import android.view.View
import de.hdodenhof.circleimageview.CircleImageView

class ChatTextWatcher(
    private val sendButton: CircleImageView,
    private val audioButton: CircleImageView,
    private val editText: EditText
) : TextWatcher {
    //до
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    //после
    override fun afterTextChanged(s: Editable) {
        val text = editText.text.toString()
        if (text.replace("\\s+".toRegex(), "").isEmpty()) {
            sendButton.visibility = View.INVISIBLE
            audioButton.visibility = View.VISIBLE
        } else {
            sendButton.visibility = View.VISIBLE
            audioButton.visibility = View.INVISIBLE
        }
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
}