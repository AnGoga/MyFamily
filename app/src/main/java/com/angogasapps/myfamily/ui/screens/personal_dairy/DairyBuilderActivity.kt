package com.angogasapps.myfamily.ui.screens.personal_dairy

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.ActivityDairyBuilderBinding
import com.angogasapps.myfamily.models.DairyObject
import com.angogasapps.myfamily.utils.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.*
import java.util.*


class DairyBuilderActivity : AppCompatActivity() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)
    lateinit var binding: ActivityDairyBuilderBinding
    var dairy: DairyObject? = null
    var hasImage: Boolean = false
    var uri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDairyBuilderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dairy = intent.extras?.get("data") as? DairyObject?

        initOnClicks()
        initFields(dairy)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun initOnClicks() {
        binding.terminalBtn.setOnClickListener {
            if (Permissions.havePermission(Permissions.WRITE_EXTERNAL_STORAGE, this))
                buildDairy()
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
    }

    private fun initFields(dairy: DairyObject?) {
        if (dairy != null) {
            binding.dateText.text = dairy.time.asDate()
            binding.smileText.text = dairy.smile
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
                Toasty.error(this, "Что-то пошло не так").show()
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

        if (title.isEmpty() && body.isEmpty() && uri == null){
            Toasty.error(this, getString(R.string.note_is_empty)).show()
            return
        }
        val key = if (this.dairy == null) UUID.randomUUID().toString() else this.dairy!!.key
        val dairy = DairyObject(key, title, body, date, "", uri.toString())

        scope.launch {
            DairyDatabaseManager.getInstance().saveDairy(dairy)
        }

        this.finish()
    }

    private fun showRemoveDairyDialog(){
        val builder = AlertDialog.Builder(this)
        with(builder){
            setTitle("Удалить заметку?")
            setMessage("Вы действительно хотите удалить эту заметку? Востановить её будет невозможно!")
            setPositiveButton("Удалить") { it, int ->
                run {
                    if (int == DialogInterface.BUTTON_POSITIVE) {
                        dairy?.let { it1 ->
                            scope.launch {
                                DairyDatabaseManager.getInstance().removeDairy(it1)
                            }
                        }
                        it.dismiss()
                        this@DairyBuilderActivity.finish()
                    }
                }
            }
            setNegativeButton("Спасти") { it, int ->
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