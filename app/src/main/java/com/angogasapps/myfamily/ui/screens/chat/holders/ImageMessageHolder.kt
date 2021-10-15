package com.angogasapps.myfamily.ui.screens.chat.holders


import com.angogasapps.myfamily.objects.ChatImageShower
import android.app.Activity
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.angogasapps.myfamily.firebase.ChatFunks
import com.angogasapps.myfamily.R

class ImageMessageHolder(rootView: View) : AppBaseViewHolder(rootView) {
    var leftImage: ImageView = rootView.findViewById(R.id.leftMessageImage)
    var rightImage: ImageView = rootView.findViewById(R.id.rightMessageImage)
    private lateinit var actualImage: ImageView
    private lateinit var imageShower: ChatImageShower

    override fun init(from: String, time: Long, messageKey: String, value: String, activity: Activity) {
        super.init(from, time, messageKey, value, activity)

        imageShower = ChatImageShower((activity as AppCompatActivity?)!!)
        val onImageClickListener = View.OnClickListener {
            imageShower.showImage(actualImage)
        }
        leftImage.setOnClickListener(onImageClickListener)
        rightImage.setOnClickListener(onImageClickListener)
    }

    override fun initLeftFields() {
        super.initLeftFields()
        actualImage = leftImage
        actualImage.transitionName = messageKey
        rightImage.transitionName = ""
        ChatFunks.downloadImageMessageAndSetBitmap(
            value,
            messageKey,
            leftImage
        )
    }

    override fun initRightFields() {
        super.initRightFields()
        actualImage = rightImage
        actualImage.transitionName = messageKey
        leftImage.transitionName = ""
        ChatFunks.downloadImageMessageAndSetBitmap(
            value,
            messageKey,
            rightImage
        )
    }

}