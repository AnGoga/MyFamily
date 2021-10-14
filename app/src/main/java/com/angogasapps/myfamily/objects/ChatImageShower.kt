package com.angogasapps.myfamily.objects

import androidx.appcompat.app.AppCompatActivity
import com.angogasapps.myfamily.objects.ChatImageShower.ImageShowerDialog
import com.github.chrisbanes.photoview.PhotoView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.angogasapps.myfamily.R
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import com.angogasapps.myfamily.databinding.DialogImageShowerBinding

class ChatImageShower(private val context: AppCompatActivity) {
    private val dialog: ImageShowerDialog
    fun showImage(imageView: ImageView) {
        dialog.setImage(imageView)
        dialog.show(context.supportFragmentManager.beginTransaction(), ImageShowerDialog.TAG)
    }

    fun dismiss() {
        dialog.dismiss()
    }

    open class ImageShowerDialog : DialogFragment() {
        private lateinit var imageView: PhotoView
        private lateinit var sharedImageView: ImageView
        private var binding: DialogImageShowerBinding? = null
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            super.onCreateView(inflater, container, savedInstanceState)
            binding = DialogImageShowerBinding.inflate(inflater, container, false)
            val rootView = inflater.inflate(R.layout.dialog_image_shower, container, false)
            imageView = rootView.findViewById(R.id.imageView)
            imageView.setImageBitmap((sharedImageView.drawable as BitmapDrawable).bitmap)
            return rootView
        }

        fun setImage(imageView: ImageView) {
            sharedImageView = imageView
        }

        override fun show(transaction: FragmentTransaction, tag: String?): Int {
            return super.show(transaction, tag)
        }

        companion object {
            const val TAG = "ImageShowerDialog"
        }
    }

    init {
        dialog = ImageShowerDialog()
    }
}