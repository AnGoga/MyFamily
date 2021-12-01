package com.angogasapps.myfamily.network.interfaces.chat

import android.widget.ImageView

interface ImageDownloader {
    fun downloadImageMessageAndSetBitmap(path: String, key: String, imageView: ImageView)
}