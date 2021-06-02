package com.angogasapps.myfamily.ui.screens.personal_dairy

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.DairyHolderBinding
import com.angogasapps.myfamily.models.DairyObject
import com.angogasapps.myfamily.objects.ChatImageShower.ImageShowerDialog.TAG
import com.angogasapps.myfamily.utils.asDate
import kotlinx.coroutines.*
import java.io.File


class DairyAdapter(private val context: Context, private var dairyList: ArrayList<DairyObject>) : RecyclerView.Adapter<DairyAdapter.DairyHolder>() {
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

    fun add(dairy: DairyObject){
        this.dairyList.add(dairy)
        notifyItemChanged(dairyList.size - 1)
    }

    class DairyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: DairyHolderBinding = DairyHolderBinding.bind(itemView)
        private lateinit var dairy: DairyObject

        fun update(dairy: DairyObject) {
            this.dairy = dairy

            binding.titleText.text = dairy.title
            binding.bodyText.text = dairy.bodyText
            binding.smileText.text = dairy.smile
            binding.dateText.text = dairy.time.asDate()

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, DairyBuilderActivity::class.java)
                intent.putExtra("data", dairy)
                binding.root.context.startActivity(intent)
            }

            if (dairy.uri.isNullOrEmpty()){
                binding.image.visibility = View.GONE
            }else{
                binding.image.visibility = View.VISIBLE
                binding.image.setImageURI(Uri.parse(dairy.uri))

            }
        }
    }
}