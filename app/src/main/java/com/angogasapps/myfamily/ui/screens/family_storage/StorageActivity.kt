package com.angogasapps.myfamily.ui.screens.family_storage

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.ActivityStorageBinding

class StorageActivity : AppCompatActivity() {
    lateinit var binding: ActivityStorageBinding
    lateinit var adapter: StorageAdapter
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initOnClicks()
    }

    private fun initOnClicks() {
        binding.floatingBtn.setOnClickListener {
            showSelectDialog()
        }
    }

    private fun initRecyclerView() {
        adapter = StorageAdapter(this)
        layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager = layoutManager
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

    }

    private fun showFileCreateDialog() {

    }
}
