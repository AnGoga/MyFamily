package com.angogasapps.myfamily.ui.screens.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.FragmentChatBinding
import com.angogasapps.myfamily.firebase.FirebaseHelper
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.objects.ChatAudioRecorder
import com.angogasapps.myfamily.objects.ChatTextWatcher
import com.angogasapps.myfamily.utils.Permissions
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import es.dmoral.toasty.Toasty
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var mRecorder: ChatAudioRecorder
    private lateinit var adapter: ChatAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var isScrollToBottom = true
    private var isScrolling = false
    private val chatManager = ChatManager.getInstance(lifecycleScope, this::addMessage)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initOnClicks()
        initRecycleView()
        initListener()
    }

    private fun initListener() {
        lifecycleScope.launch(Dispatchers.IO) {
            chatManager.listener.collect {
                withContext(Dispatchers.Main) {
                    adapter.bindEvent(it)
                }
            }
        }
    }


    private fun addMessage(start: Int, end: Int) = runOnUiThread {
        println("update range [$start; $end]")
        adapter.addInRange(start, end)
        if (isScrollToBottom)
            binding.recycleView.smoothScrollToPosition(adapter.itemCount)
    }


    private fun initRecycleView() {
        adapter = ChatAdapter(this, chatManager.messagesList)
        layoutManager = object: LinearLayoutManager(this){
            override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
                try {
                    super.onLayoutChildren(recycler, state)
                } catch (e: IndexOutOfBoundsException) {}
            }
        }
        layoutManager.stackFromEnd = true

        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager = layoutManager
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isScrolling && dy < 0 && layoutManager.findFirstVisibleItemPosition() <= dangerFirstVisibleItemPosition) {
                    isScrollToBottom = false
                    isScrolling = false
                    chatManager.getMoreMessage()
                }
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initOnClicks() {
        binding.chatEditText.addTextChangedListener(
                ChatTextWatcher(binding.sendMessageBtn, binding.audioBtn, binding.chatEditText))

        binding.sendMessageBtn.setOnClickListener { v ->
            isScrollToBottom = true
            chatManager.sendMessage(TYPE_TEXT_MESSAGE, binding.chatEditText.text.toString())
            binding.chatEditText.setText("")
            binding.recycleView.smoothScrollToPosition(adapter.getItemCount())
        }

        binding.audioBtn.setOnTouchListener { v, event ->
            if (Permissions.havePermission(Permissions.AUDIO_RECORD_PERM, this)) {
                if (event.action === MotionEvent.ACTION_DOWN) {
                    mRecorder = ChatAudioRecorder(this, FirebaseHelper.messageKey)
                    mRecorder.startRecording()
                } else if (event.action === MotionEvent.ACTION_UP) {
                    mRecorder.stopRecording {
                        mRecorder.file?.let {
                            chatManager.sendVoice(it, mRecorder.key)
                        }
                    }
                }
            }
            true
        }

        binding.clipBtn.setOnClickListener { v -> getPhotoUri() }
    }

    private fun getPhotoUri() {
        CropImage.activity().setAspectRatio(1, 1)
                .setRequestedSize(
                        resources.getInteger(R.integer.image_message_wight),
                        resources.getInteger(R.integer.image_message_height)
                )
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == RESULT_OK) {
            val photoUri = CropImage.getActivityResult(data).uri
            if (photoUri != null)
                chatManager.sendImage(photoUri)
            else
                Toasty.error(this, R.string.something_went_wrong).show()
        }
    }
}