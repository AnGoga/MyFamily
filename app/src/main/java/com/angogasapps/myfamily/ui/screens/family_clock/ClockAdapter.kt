package com.angogasapps.myfamily.ui.screens.family_clock

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.ClockViewHolderBinding
import com.angogasapps.myfamily.firebase.EFirebaseEvents
import com.angogasapps.myfamily.models.Family
import com.angogasapps.myfamily.models.family_clock.ClockObject
import com.angogasapps.myfamily.models.family_clock.FamilyClockEvent
import com.angogasapps.myfamily.utils.StringFormater
import com.angogasapps.myfamily.utils.asDate

class ClockAdapter(val context: Context): RecyclerView.Adapter<ClockAdapter.ClockViewHolder>() {
    val inflater: LayoutInflater = LayoutInflater.from(context)

    val list = ArrayList<ClockObject>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClockViewHolder {
        return ClockViewHolder(inflater.inflate(R.layout.clock_view_holder, parent, false))
    }

    override fun onBindViewHolder(holder: ClockViewHolder, position: Int) {
        holder.update(list[position])
    }

    override fun getItemCount() = list.size

    fun update(event: FamilyClockEvent) = synchronized(list){
        if(event.event == EFirebaseEvents.added) {
                list.add(event.value)
                notifyItemInserted(itemCount - 1)
                return@synchronized
            }

        for (i in list){
            if (i.id == event.value.id) {
                val index = list.indexOf(i)

                when (event.event) {
                    EFirebaseEvents.changed -> {
                        list[index] = event.value
                        notifyItemChanged(index)
                    }
                    EFirebaseEvents.removed -> {
                        list.removeAt(index)
                        notifyItemRemoved(index)
                    }
                }
            }
        }
    }

   inner class ClockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ClockViewHolderBinding.bind(itemView)

        fun update(obj: ClockObject) {
            binding.timeText.text = StringFormater.formatLongToTime(obj.time)
            binding.dateText.text = obj.time.asDate()
            binding.from.text = context.getString(R.string.clock_is_on_by) + Family.getInstance().getNameByPhone(obj.fromPhone)
        }
    }
}