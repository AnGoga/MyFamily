package com.angogasapps.myfamily.ui.screens.family_storage

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.ActivityStorageBinding
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_BASE_FOLDER
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_NODE
import com.angogasapps.myfamily.ui.screens.family_storage.dialogs.CreateFolderDialog
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class StorageActivity : AppCompatActivity() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    lateinit var binding: ActivityStorageBinding
    lateinit var adapter: StorageAdapter
    lateinit var layoutManager: LinearLayoutManager

    lateinit var ROOT_NODE: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        analyzeIntent()
        initRecyclerView()
        initOnClicks()
    }

    private fun analyzeIntent() {
        ROOT_NODE = intent.getStringExtra(TYPE_NODE)
    }

    private fun initOnClicks() {
        binding.floatingBtn.setOnClickListener {
            showSelectDialog()
        }
    }

    private fun initRecyclerView() {
        adapter = StorageAdapter(this) { name: String ->
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
            StorageManager.getInstance().getData(ROOT_NODE).collect { isSuccess ->
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
                rootNode = ROOT_NODE,
                rootFolder = if (adapter.stack.empty()) CHILD_BASE_FOLDER else adapter.stack.peek())
    }

    private fun showFileCreateDialog() {

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
