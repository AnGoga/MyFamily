package com.angogasapps.myfamily.ui.screens.personal_dairy

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.DairyHolderBinding
import com.angogasapps.myfamily.models.DairyObject
import com.angogasapps.myfamily.utils.asDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class DairyAdapter(private val context: Context, private var dairyList: ArrayList<DairyObject>,
                   val scope: CoroutineScope) : RecyclerView.Adapter<DairyAdapter.DairyHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DairyHolder {
        return DairyHolder(inflater.inflate(R.layout.dairy_holder, parent, false))
    }

    override fun onBindViewHolder(holder: DairyHolder, position: Int) {
        holder.update(dairyList[position])
    }

    override fun getItemCount(): Int {
        return this.dairyList.size
    }

    fun addAll(list: List<DairyObject>){
        val start = dairyList.size
        this.dairyList.addAll(list)
        notifyItemRangeInserted(start, list.size)
    }

    fun update(event: DairyEvent){
        when (event.events){
            EDairyEvents.add -> {this.notifyItemInserted(event.index)}
            EDairyEvents.remove -> {this.notifyItemRemoved(event.index)}
        }
    }

    inner class DairyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: DairyHolderBinding = DairyHolderBinding.bind(itemView)
        private lateinit var dairy: DairyObject

        fun update(dairy: DairyObject) {
            this.dairy = dairy

            if (dairy.uri == "null"){
                binding.image.visibility = View.GONE
            }else{
                binding.image.visibility = View.VISIBLE
                scope.launch(Dispatchers.IO) {
                    val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
                    val myDir = File("$root/dairy_images")
                    val file = File(myDir, dairy.uri.split("/").last())
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    withContext(Dispatchers.Main) {
                        binding.image.setImageBitmap(bitmap)
                    }
                }
            }

            binding.titleText.text = dairy.title
            binding.bodyText.text = dairy.bodyText
            binding.smileText.text = dairy.smile
            binding.dateText.text = dairy.time.asDate()

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, DairyBuilderActivity::class.java)
                intent.putExtra("data", dairy)
                binding.root.context.startActivity(intent)
            }


        }
    }
}
