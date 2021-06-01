package com.angogasapps.myfamily.ui.screens.personal_dairy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.ActivityDairyBuilderBinding
import com.angogasapps.myfamily.models.DairyObject
import com.angogasapps.myfamily.utils.Permissions
import com.angogasapps.myfamily.utils.asDate
import com.angogasapps.myfamily.utils.asMillis
import com.angogasapps.myfamily.utils.toEditable
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class DairyBuilderActivity : AppCompatActivity() {
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

    private fun initOnClicks() {
        binding.terminalBtn.setOnClickListener {
            buildDairy()
        }

        binding.removeImage.setOnClickListener {
            if (hasImage){
                binding.image.setImageBitmap(null)
                hasImage = false
            }else{}
        }
        binding.addImage.setOnClickListener {
            if (hasImage){
                Toasty.warning(this, getString(R.string.image_added_yet)).show()
            }else{
                getAndSetImage()
            }
        }
    }

    private fun initFields(dairy: DairyObject?) {
        if (dairy != null) {
            binding.dateText.text = dairy.time.asDate()
            binding.smileText.text = dairy.smile
            binding.titleEditText.text = dairy.title.toEditable()
            binding.bodyEditText.text = dairy.bodyText.toEditable()


            if (dairy.uri.isNotEmpty()) {
                binding.image.setImageURI(Uri.parse(dairy.uri))
                hasImage = true
            }
        }else{
            binding.dateText.text = System.currentTimeMillis().asDate()
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

    private fun buildDairy(){

        val title: String = binding.titleEditText.text.toString()
        val body: String = binding.bodyEditText.text.toString()
        val date: Long = binding.dateText.text.toString().asMillis()
        val uri: Uri? = if (hasImage) uri else null

        if (title.isEmpty() && body.isEmpty() && uri == null){
            Toasty.error(this, getString(R.string.note_is_empty)).show()
            return
        }

        val dairy = DairyObject(0, title, body, date, "", uri.toString())

        CoroutineScope(Dispatchers.Default).launch {
            DairyDatabaseManager.getInstance().saveDairy(dairy)
        }
        this.finish()
    }
}