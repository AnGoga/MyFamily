package com.angogasapps.myfamily.ui.screens.personal_dairy

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.databinding.ActivityDairyBuilderBinding
import com.angogasapps.myfamily.models.DairyObject
import com.angogasapps.myfamily.objects.ChatImageShower
import com.angogasapps.myfamily.utils.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject


class DairyBuilderActivity : AppCompatActivity() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)
    lateinit var binding: ActivityDairyBuilderBinding
    var dairy: DairyObject? = null
    var hasImage: Boolean = false
    var uri: Uri? = null

    @Inject lateinit var dairyDatabaseManager: DairyDatabaseManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDairyBuilderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appComponent.inject(this)

        dairy = intent.extras?.get("data") as? DairyObject?

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        initOnClicks()
        initFields(dairy)
        initSpinner()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun initOnClicks() {
        binding.terminalBtn.setOnClickListener {
            if (Permissions.havePermission(Permissions.WRITE_EXTERNAL_STORAGE, this))
                if (Permissions.hasStoragePermission(this)) {
                    buildDairy()
                } else {
                    Toasty.error(this, getString(R.string.needed_permission_not_got)).show()
                }
        }

        binding.removeImage.setOnClickListener {
            if (hasImage){
                binding.image.setImageBitmap(null)
                hasImage = false
                uri = null
            }
        }
        binding.addImage.setOnClickListener {
            if (hasImage){
                Toasty.warning(this, getString(R.string.image_added_yet)).show()
            }else{
                getAndSetImage()
            }
        }
        binding.dateText.setOnClickListener {
            getDayDate()
        }
        binding.image.setOnClickListener {
            if (hasImage){
                ChatImageShower(this).showImage(binding.image)
            }
        }
    }

    private fun initFields(dairy: DairyObject?) {
        if (dairy != null) {
            binding.dateText.text = dairy.time.asDate()
            binding.titleEditText.text = dairy.title.toEditable()
            binding.bodyEditText.text = dairy.bodyText.toEditable()

            if (dairy.uri != "null") {
                uri = Uri.parse(dairy.uri)
                binding.image.setImageURI(uri)
                hasImage = true
            }
            binding.removeBtn.setOnClickListener {
                showRemoveDairyDialog()
            }
        }else{
            binding.dateText.text = System.currentTimeMillis().asDate()
            binding.removeBtn.visibility = View.GONE
        }
    }

    private fun initSpinner() {
        val adapter = ArrayAdapter.createFromResource(this, R.array.smiles_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.smileSpinner.adapter = adapter

        if (dairy != null) {
            val smileList = resources.getStringArray(R.array.smiles_array)
            val index = smileList.indexOf(dairy!!.smile)
            binding.smileSpinner.setSelection(index)

            binding.smileSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun getAndSetImage() {
        CropImage.activity()
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == RESULT_OK) {
            val uri = CropImage.getActivityResult(data).uri
            if (uri != null) {
                binding.image.setImageURI(uri)
                hasImage = true
                this.uri = uri
            }else
                Toasty.error(this, R.string.something_went_wrong).show()
        }
    }

    private fun getDayDate() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this@DairyBuilderActivity, { view: View, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            run {
                binding.dateText.text = toDate(year, monthOfYear + 1, dayOfMonth)
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show()
    }

    private fun buildDairy(){
        val title: String = binding.titleEditText.text.toString()
        val body: String = binding.bodyEditText.text.toString()
        val date: Long = binding.dateText.text.toString().asMillis()
        val uri: Uri? = if (hasImage) uri else null
        val smile: String = binding.smileSpinner.selectedItem.toString()

        if (title.isEmpty() && body.isEmpty() && uri == null){
            Toasty.error(this, getString(R.string.note_is_empty)).show()
            return
        }
        val key = if (this.dairy == null) UUID.randomUUID().toString() else this.dairy!!.key
        val dairy = DairyObject(key, title, body, date, smile, uri.toString())

        scope.launch {
            dairyDatabaseManager.saveDairy(dairy)
        }

        this.finish()
    }

    private fun showRemoveDairyDialog(){
        val builder = AlertDialog.Builder(this)
        with(builder){
            setTitle(getString(R.string.remove_note_question))
            setMessage(getString(R.string.accept_remove_dairy))
            setPositiveButton(R.string.remove) { it, int ->
                run {
                    if (int == DialogInterface.BUTTON_POSITIVE) {
                        dairy?.let { it1 ->
                            scope.launch {
                                dairyDatabaseManager.removeDairy(it1)
                            }
                        }
                        it.dismiss()
                        this@DairyBuilderActivity.finish()
                    }
                }
            }
            setNegativeButton(getString(R.string.rescue)) { it, int ->
                run {
                    if (int == DialogInterface.BUTTON_NEGATIVE) {
                        it.dismiss()
                    }
                }
            }
        }

        val dialog = builder.create();

        dialog.show()
    }
}