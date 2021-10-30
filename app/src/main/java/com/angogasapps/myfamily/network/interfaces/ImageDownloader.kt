package com.angogasapps.myfamily.network.interfaces

import android.widget.ImageView

interface ImageDownloader {
    fun downloadImageMessageAndSetBitmap(path: String, key: String, imageView: ImageView)
}