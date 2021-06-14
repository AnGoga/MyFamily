package com.angogasapps.myfamily.ui.screens.family_storage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.angogasapps.myfamily.databinding.ActivityStorageBinding
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*
import com.angogasapps.myfamily.ui.screens.family_storage.dialogs.CreateFolderDialog
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class StorageActivity : AppCompatActivity() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    lateinit var binding: ActivityStorageBinding
    lateinit var adapter: FileStorageAdapter
    lateinit var layoutManager: LinearLayoutManager

    lateinit var rootNode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        analyzeIntent()
        initRecyclerView()
        initOnClicks()
    }

    private fun analyzeIntent() {
        rootNode = intent.getStringExtra(TYPE_NODE)
    }

    private fun initOnClicks() {
        binding.floatingBtn.setOnClickListener {
            showSelectDialog()
        }
    }

    private fun initRecyclerView() {
        adapter = FileStorageAdapter(this, rootNode = rootNode) { name: String ->
            run {
                this@StorageActivity.title = name
            }
        }
        layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager = layoutManager
        updateRecyclerView()
    }

    private fun updateRecyclerView() {
        scope.launch {
            StorageManager.getInstance().getData(rootNode).collect { isSuccess ->
                if (!isSuccess) return@collect
                withContext(Dispatchers.Main){ adapter.update() }
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
        CreateFolderDialog(this).show(
                rootNode = rootNode,
                rootFolder = adapter.getRootFolder())
    }

    private fun showFileCreateDialog() {
        when(rootNode){
//            NODE_IMAGE_STORAGE -> {
//               startActivity(
//                       Intent(this, CreateImageFileActivity::class.java)
//                       .also { it.putExtra(ROOT_FOLDER, adapter.getRootFolder()) }
//               )
//            }
            NODE_NOTE_STORAGE -> {
                startActivity(Intent(this, StorageNoteBuilderActivity::class.java)
                        .also { it.putExtra(ROOT_FOLDER, adapter.getRootFolder()) })
            }
        }
    }

    override fun onBackPressed() {
        if (!adapter.exitFromUpFolder()){
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
