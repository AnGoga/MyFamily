package com.angogasapps.myfamily.ui.screens.family_storage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.databinding.ActivityStorageNoteBuilderBinding
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.network.interfaces.family_stoarge.FamilyStorageService
import es.dmoral.toasty.Toasty
import javax.inject.Inject

class StorageNoteBuilderActivity : AppCompatActivity() {
    lateinit var binding: ActivityStorageNoteBuilderBinding
    lateinit var rootFolder: String
    private var id: String? = null
    private var name: String? = null
    private var value: String? = null
    @Inject
    lateinit var storageService: FamilyStorageService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorageNoteBuilderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appComponent.inject(this)
        analyzeIntent()
        initOnClicks()
    }

    private fun analyzeIntent() {
        rootFolder = intent.extras?.getString(ROOT_FOLDER)?: CHILD_BASE_FOLDER
        id = intent.extras?.get(CHILD_ID) as String?
        name = intent.extras?.get(CHILD_NAME) as String?
        value = intent.extras?.get(CHILD_VALUE) as String?
        if (id != null) initFields()
    }

    private fun initFields() {
        binding.nameEditText.setText(name)
        binding.noteEditText.setText(value)
    }

    private fun initOnClicks() {
        binding.buttonSave.setOnClickListener {
            val title = binding.nameEditText.text.toString().trim()
            val value = binding.noteEditText.text.toString().trim()

            if (title.isNotEmpty()){
                saveNote(title, value)
                finish()
            }
        }
    }

    private fun saveNote(title: String, value: String) {
        storageService.createFile(
                name = title, value = value, rootNode = NODE_NOTE_STORAGE, rootFolder = rootFolder,
                key_ = id,
                onError = { Toasty.error(this, R.string.something_went_wrong).show() }
        )
    }
}
