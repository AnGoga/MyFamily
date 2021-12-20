package com.angogasapps.myfamily.ui.screens.family_storage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.databinding.ActivityStorageBinding
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.network.interfaces.family_stoarge.FamilyStorageService
import com.angogasapps.myfamily.ui.screens.family_storage.storage_adapters.*
import com.angogasapps.myfamily.ui.screens.family_storage.dialogs.NameGetterDialog
import com.angogasapps.myfamily.utils.FILE_SELECT_CODE
import com.angogasapps.myfamily.utils.showFileChooser
import kotlinx.coroutines.*
import javax.inject.Inject


class StorageActivity : AppCompatActivity() {
    lateinit var binding: ActivityStorageBinding
    lateinit var adapter: BaseStorageAdapter
    lateinit var layoutManager: LinearLayoutManager
    @Inject
    lateinit var storageService: FamilyStorageService
    @Inject
    lateinit var viewModel: StorageViewModel

    lateinit var rootNode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appComponent.inject(this)

        analyzeIntent()
        initRecyclerView()
        initSwipeRefresh()
        initOnClicks()

    }

    private fun initSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            updateRecyclerView()
        }
    }

    private fun analyzeIntent() {
        rootNode = intent.getStringExtra(TYPE_NODE)?:""
    }

    private fun initOnClicks() {
        binding.floatingBtn.setOnClickListener {
            if (rootNode == NODE_IMAGE_STORAGE || rootNode == NODE_VIDEO_STORAGE) {
                showFolderCreateDialog()
            }else{
                showSelectDialog()
            }
        }
    }

    private fun initRecyclerView() {
        val onChangeDirectory = { name: String -> this@StorageActivity.title = name }

        adapter = when(rootNode){
            NODE_NOTE_STORAGE -> NoteStorageAdapter(this, rootNode, onChangeDirectory, storageService)
            NODE_FILE_STORAGE -> FileStorageAdapter(this, rootNode, onChangeDirectory, storageService)
            NODE_VIDEO_STORAGE, NODE_IMAGE_STORAGE -> MediaStorageAdapter(this, rootNode, onChangeDirectory, storageService)
            else -> BaseStorageAdapter(this, rootNode, onChangeDirectory, storageService)
        }

        layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager = layoutManager
        updateRecyclerView()

    }

    private fun updateRecyclerView() {
        binding.swipeRefresh.isRefreshing = true
        lifecycleScope.launch(Dispatchers.IO) {
            val isSuccess = viewModel.getData(rootNode)
            if (!isSuccess) return@launch
            withContext(Dispatchers.Main) {
                adapter.update()
                binding.swipeRefresh.isRefreshing = false
            }

        }
    }

    private fun showSelectDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Что вы хотите добавить?")
        builder.setMessage("Что вы хотите добавить?")
        builder.setPositiveButton("Папка") { dialog, which: Int ->
            run {
                if (which == AlertDialog.BUTTON_POSITIVE) {
                    showFolderCreateDialog()
                }
            }
        }
        builder.setNegativeButton("Файл") { dialog, which: Int ->
            run {
                if (which == AlertDialog.BUTTON_NEGATIVE) {
                    showFileCreateDialog()
                }
            }
        }
        builder.create().show()
    }


    private fun showFolderCreateDialog() {
        NameGetterDialog(this).show(isFolder = true) { name ->
            storageService.createFolder(name = name, rootNode = rootNode, rootFolder = adapter.getRootFolderId())
        }
    }

    private fun showFileCreateDialog() {
        when(rootNode){
            NODE_NOTE_STORAGE -> {
                startActivity(Intent(this, StorageNoteBuilderActivity::class.java)
                        .also { it.putExtra(ROOT_FOLDER, adapter.getRootFolderId()) })
            }
            NODE_FILE_STORAGE -> {
                showFileChooser(this)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FILE_SELECT_CODE -> if (resultCode == RESULT_OK) {
                val uri = data?.data ?: return
                NameGetterDialog(this).show(isFolder = false){
                    storageService.createStorageFile(uri = uri, name = it, rootFolderId = adapter.getRootFolderId())
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (!adapter.exitFromUpFolder()){
            super.onBackPressed()
        }
    }
}
